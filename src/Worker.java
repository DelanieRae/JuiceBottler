public class Worker {
    private final Plant plant;
    

    public Worker(Plant plant) {
        this.plant = plant;
    }
    //will call processOrange until the bottle state is Bottled
    public void processEntireOrange(Orange o) {
        do {
            processOrange(o);
        } while (o.getState() != Orange.State.Bottled);
        plant.completeOrange(o);
    }
    //Process orange.  Plant[1] Worker[2] & Plant[2] Worker[2] will Squeeze the Orange
    public void processOrange(Orange o) {

    	if((o.getState()== Orange.State.Peeled)&&((plant.plantNum==1)&&(plant.workNum==2))){
    			o.runSqueezed();
    	}
    	if((o.getState()== Orange.State.Peeled)&&((plant.plantNum==2)&&(plant.workNum==2))){
    			o.runSqueezed();
    	}else {
    		if(o.getState()!=Orange.State.Peeled || o.getState()!=Orange.State.Bottled) {
    			o.runProcess();
    		}
    	}
    }
}
