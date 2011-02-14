package edu.atilim.acma.search;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import edu.atilim.acma.RunConfig;
import edu.atilim.acma.concurrent.Command;
import edu.atilim.acma.concurrent.Instance;
import edu.atilim.acma.concurrent.InstanceSet;
import edu.atilim.acma.concurrent.TaskInterruptedException;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.util.Delegate;

public abstract class ConcurrentMultiRunAlgorithm extends ConcurrentAlgorithm {
	private int runCount;
	
	public ConcurrentMultiRunAlgorithm() {
		
	}
	
	public ConcurrentMultiRunAlgorithm(String name, RunConfig config, Design initialDesign, int runCount) {
		super(name, config, initialDesign);
		
		this.runCount = runCount;
	}
	
	private transient int completed;
	private transient int assigned;

	@Override
	public void runMaster(InstanceSet instances) {
		System.out.printf("Master process started for %s\n", getName());
		
		completed = assigned = 0;
		
		final Delegate.Void1P<Instance> assigner = new Delegate.Void1P<Instance>() {
			@Override
			public void run(Instance in) {
				in.send(new StartCommand());
				assigned++;
				
				System.out.printf("Assigned task to %s. Remaining: %d.\n", in.getName(), runCount - assigned);
			}
		};
		
		final Delegate.Void1P<Instance> finalizer = new Delegate.Void1P<Instance>() {
			@Override
			public void run(Instance in) {
				in.send(new StopCommand());
				
				System.out.printf("Finalized instance %s.\n", in.getName());
			}
		};
		
		for (Instance i : instances) {
			if (assigned < runCount) {
				assigner.run(i);
			} else {
				finalizer.run(i);
			}
		}
		
		while(true) {
			if (isInterrupted())
				throw new TaskInterruptedException();
			
			for (Instance i : instances) {
				Design fdesign = i.tryReceive(Design.class);
				
				if (fdesign != null) {
					System.out.printf("Received design from %s.\n", i.getName());
					
					if (assigned < runCount) {
						assigner.run(i);
					} else {
						finalizer.run(i);
					}
					
					completed++;
					onFinish(fdesign);
					
					if (completed == runCount)
						return;
				}
			}
			
			try { Thread.sleep(50);	} catch (InterruptedException e) {}
		}
	}

	@Override
	public void runWorker(final Instance master) {
		System.out.printf("Worker process started for %s\n", getName());
		
		final AlgorithmObserver observer = new AlgorithmObserver() {
			
			@Override
			public void onUpdateItems(AbstractAlgorithm algorithm,
					SolutionDesign current, SolutionDesign best, int updateType) {
			}
			
			@Override
			public void onStart(AbstractAlgorithm algorithm, SolutionDesign initial) {
			}
			
			@Override
			public void onLog(AbstractAlgorithm algorithm, String log) {
				System.out.println(log);
			}
			
			@Override
			public void onFinish(AbstractAlgorithm algorithm, SolutionDesign last) {
				System.out.println("Pushing result to master.");
				master.send(last.getDesign());
			}
			
			@Override
			public void onExpansion(AbstractAlgorithm algorithm, int currentExpanded,
					int totalExpanded) {
			}
			
			@Override
			public void onAdvance(AbstractAlgorithm algorithm, int current, int total) {
			}
			
			@Override
			public void onStep(AbstractAlgorithm algorithm, int step) {
				if (isInterrupted())
					throw new TaskInterruptedException();
			}
		};
		
		while(true)
		{
			Command command = master.receive(Command.class);
			
			if (command.getCommand().equals(StopCommand.COMMAND))
				break;
			
			AbstractAlgorithm algorithm = spawnAlgorithm();
			algorithm.setObserver(observer);
			algorithm.start(false);
		}
	}
	
	public abstract AbstractAlgorithm spawnAlgorithm();
	
	public static class StartCommand implements Command {
		private static final long serialVersionUID = 1L;
		public static final String COMMAND = "START";
		
		@Override
		public String getCommand() {
			return COMMAND;
		}
	}
	
	public static class StopCommand implements Command {
		public static final String COMMAND = "STOP";
		private static final long serialVersionUID = 1L;
		
		@Override
		public String getCommand() {
			return COMMAND;
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		
		out.writeInt(0); //version
		out.writeInt(runCount);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		
		in.readInt();
		runCount = in.readInt();
	}
}
