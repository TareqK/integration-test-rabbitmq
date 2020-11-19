/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kisoft;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "stop-rabbitmq", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class StopRabbitMQMojo extends AbstractMojo {

    private final DockerClient docker = DockerClientBuilder.getInstance().build();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("Starting RabbitMQ container with name " + State.instance().getName());
        docker.stopContainerCmd(State.instance().getContainerId());
        System.out.println("Stopped RabbitMQ container with name " + State.instance().getName());
        System.out.println("Removing RabbitMQ container with name " + State.instance().getName());
        docker.removeContainerCmd(State.instance().getContainerId()).exec();
        System.out.println("Removed RabbitMQ container with name " + State.instance().getName());
    }

}
