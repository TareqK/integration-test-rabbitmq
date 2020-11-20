package me.kisoft;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.api.model.ResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;
import java.io.Closeable;
import java.io.IOException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import static me.kisoft.Utils.waitFor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "start-rabbitmq", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class StartRabbitMQMojo extends AbstractMojo {

    private final DockerClient docker = DockerClientBuilder.getInstance().build();
    /**
     * The port to bind rabbitmq to
     */
    @Getter
    @Setter
    @Parameter(property = "port", required = false, defaultValue = "5672")
    private int port = 5672;

    /**
     * The name of the docker container to use
     */
    @Getter
    @Setter
    @Parameter(property = "name", required = false, defaultValue = "Random Alphanumeric String")
    private String name = RandomStringUtils.randomAlphabetic(15);

    /**
     * The name of the rabbitmq docker image to pull and use
     */
    @Getter
    @Setter
    @Parameter(property = "image", required = false, defaultValue = "rabbitmq:3-management")
    private String image = "rabbitmq:3-management";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            State.instance().setPort(port);
            State.instance().setName(name);
            PullCallback pullCallback = new PullCallback();
            docker.pullImageCmd(image).exec(pullCallback);
            waitFor(pullCallback::isComplete, 200);
            if (pullCallback.isFailed()) {
                throw new MojoFailureException("Failed To pull docker image");
            }
            System.out.println("Creating RabbitMQ container with name " + name);
            String id = docker.createContainerCmd(image)
                    .withName(name)
                    .withPortBindings(new PortBinding(Binding.bindPort(port), new ExposedPort(5672)))
                    .withTty(true)
                    .exec().getId();
            System.out.println("Created RabbitMQ container with name " + name);
            System.out.println("Starting RabbitMQ instance on port " + port);
            docker.startContainerCmd(id).exec();
            System.out.println("Started RabbitMQ instance on port " + port);
            State.instance().setContainerId(id);
        } catch (InterruptedException ex) {
            throw new MojoExecutionException(ex.getMessage());
        }
    }

    @Data
    private class PullCallback implements ResultCallback {

        private boolean complete = false;
        private boolean failed = false;

        @Override
        public void onStart(Closeable closeable) {
            System.out.println("Starting Docker Image Pull");
        }

        @Override
        public void onNext(Object object) {
            if (object instanceof ResponseItem) {
                ResponseItem item = (ResponseItem) object;
                System.out.println(item.getStatus() + (item.getId() != null ? " " + item.getId() : ""));
            } else {
                System.out.println(object);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            System.err.println(throwable.getMessage());
            this.failed = true;
            this.complete = true;
        }

        @Override
        public void onComplete() {
            this.failed = false;
            this.complete = true;
        }

        @Override
        public void close() throws IOException {
        }

    }

}
