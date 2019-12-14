package m;

public class SpellCard {
	
	public static final int Easy=0;
	public static final int Normal=1;
	public static final int Hard=2;
	public static final int Lunatic=3;
	public static final int Extra=4;
	public static final int Phamtasm=5;
	public static final int LastSpell=6;
	public static final int Lastword=7;
	public static final int Overdrive=8;
	
	public int diffcult=0;
	public String name;
	public String master;

	public SpellCard(String name, String master, int diffcult) {
		this.name = name;
		this.master = master;
		this.diffcult = diffcult;
	}

	public SpellCard(String firstName, String lastName, String master, int diffcult) {
		name = firstName + "「" + lastName + "」";
		this.master = master;
		this.diffcult = diffcult;
	}

}
