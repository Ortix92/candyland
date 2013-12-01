import java.util.ArrayList;

import javax.media.opengl.GL;



public class Enemies {

private ArrayList<TestBox> nyans;
double aantal;

public Enemies() {
	nyans = new ArrayList<TestBox>();
	aantal = 0;
}

public void addNyan(TestBox nyan) {
	nyans.add(nyan);
	aantal = aantal + 1;
}

public void Nyanhealth() {
	for (int i = 0; i < aantal; i++) {
		if (nyans.get(i).getHealth() <= 0) {
			nyans.remove(nyans.get(i));
		}
	}
}


public boolean isNyan(double x, double z) {
	for (int i = 0; i < aantal; i++) {
	if(( x >= nyans.get(i).getLocationX() - 5 && x <= nyans.get(i).getLocationX() + 5) || (z >= nyans.get(i).getLocationZ() 
			&& z <= nyans.get(i).getLocationZ())) {
		nyans.get(i).setHealth(nyans.get(i).getHealth()-10);
		return true;
	
}
}
return false;
}

public void display(GL gl) {
	for (int i = 0; i < aantal; i++) {
		nyans.get(i).display(gl);
	}
}

public TestBox get(int i) {
	return nyans.get(0);
}
}




