public class Orange {
	//Creates states
    public enum State {
        Fetched(15),
        Peeled(38),
        Squeezed(29),
        Bottled(17),
        Processed(1);

        private static final int finalIndex = State.values().length - 1;

        final int timeToComplete;
        
        //Gives timeToComplete
        State(int timeToComplete) {
            this.timeToComplete = timeToComplete;
        }

        //Sets state to the next state when called
        State getNext() {
            int currIndex = this.ordinal();
            if (currIndex >= finalIndex) {
                throw new IllegalStateException("Already at final state");
            }
            return State.values()[currIndex + 1];
        }
    }

    private State state;

    //Gets First Orange. Sets Orange's state to fetched.
    public Orange() {
        state = State.Fetched;
        doWork();
    }

    //Returns current state of the Orange
    public State getState() {
        return state;
    }
    public void SetFetch() {
    	state = State.Fetched;
    	doWork();
    	}
    //Does all steps besides the 
    public void runProcess() {
        // Don't attempt to process an already completed orange
        if (state == State.Processed) {
            throw new IllegalStateException("This orange has already been processed");
        }
        state = state.getNext();
        doWork();
    }
    public void runSqueezed() {
    	if (state == State.Processed) {
            throw new IllegalStateException("This orange has already been processed");
        }
        state = state.getNext();
        doWork();
    }
    private void doWork() {
        // Sleep for the amount of time necessary to do the work
        try {
            Thread.sleep(state.timeToComplete);
        } catch (InterruptedException e) {
            System.err.println("Incomplete orange processing, juice may be bad");
        }
    }
}