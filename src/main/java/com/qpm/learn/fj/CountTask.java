package com.qpm.learn.fj;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * 这个框架也不能滥用，因为创建线程奔上就是一个大的消耗。任务分割阀值要足够大，考虑到线程的使用效率才决定使用这个框架
 * 
 * @author qpm
 *
 */
public class CountTask extends RecursiveTask<Integer>{

	private static final int THRESHOLD = 2;
	private int start;
	private int end;
	
	public CountTask(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	
	
	@Override
	protected Integer compute() {
		int sum = 0;

        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) {
        	for (int i = start; i <= end; i++) {
        		sum += i;
        	}
        } else {
        	int middle = (start + end) / 2;
        	CountTask leftTask = new CountTask(sum, middle);
        	CountTask rightTask = new CountTask(middle + 1, end);
        	leftTask.fork();
        	rightTask.fork();
        	
        	int leftResult = leftTask.join();
        	int rightResult = rightTask.join();
        	
        	sum = leftResult + rightResult;
        }

		return sum;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ForkJoinPool pool = new ForkJoinPool();
		CountTask task = new CountTask(1, 8);
		long before = System.currentTimeMillis();
		Future<Integer> results = pool.submit(task);
		System.out.println(results.get());
		System.out.println(System.currentTimeMillis() - before);
	}
	

}
