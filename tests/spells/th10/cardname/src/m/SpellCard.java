package m;

public class SpellCard {

	public static final int Easy=1 << 0;
	public static final int Normal=1 << 1;
	public static final int Hard=1 << 2;
	public static final int Lunatic=1 << 3;
	public static final int Extra=1 << 4;
	public static final int Phantasm=1 << 5;
	public static final int LastSpell=1 << 6;
	public static final int Lastword=1 << 7;
	public static final int Overdrive=1 << 8;

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
