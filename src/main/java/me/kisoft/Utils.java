/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kisoft;

/**
 *
 * @author tareq
 */
public class Utils {

    public static void waitFor(WaitCondition condition, long pollTime) throws InterruptedException {
        while (!condition.isComplete()) {
            Thread.sleep(pollTime);
        }
    }

    public interface WaitCondition {

        public boolean isComplete();
    }
}
