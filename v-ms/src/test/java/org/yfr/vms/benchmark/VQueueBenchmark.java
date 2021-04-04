
package org.yfr.vms.benchmark;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.yfr.vms.common.VElement;
import org.yfr.vms.vqueue.VQueue;

@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class VQueueBenchmark {

	private static VQueue vQueue = new VQueue("vQueue", false, "vQueue", false, 2000, 1000, 0);
	private static VQueue vLoggingQueue = new VQueue("vLoggingQueue", true, "vLoggingQueue", false, 2000, 1000, 0);
	private static DelayQueue<VElement> simpleDelayQueue = new DelayQueue<VElement>();

	@Benchmark
	public VElement testVQueue() {
		vQueue.put(new VElement("message"));
		return vQueue.take();
	}

	@Benchmark
	public VElement testVLoggingQueue() {
		vLoggingQueue.put(new VElement("message"));
		return vLoggingQueue.take();
	}

	@Benchmark
	public VElement testSimpleDelayQueue() throws InterruptedException {
		simpleDelayQueue.put(new VElement("message"));
		return simpleDelayQueue.take();
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(VQueueBenchmark.class.getSimpleName()).warmupIterations(3).measurementIterations(3).forks(1).build();

		new Runner(opt).run();
	}

}
