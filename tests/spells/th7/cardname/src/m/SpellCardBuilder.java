package m;
import java.util.*;

public class SpellCardBuilder {
	private ArrayList<SpellCard> sclist=new ArrayList<>();
	private String master=null;
	public SpellCardBuilder() {

	}

	public SpellCardBuilder setMaster(String master) {
		this.master = master;
		return this;
	}

	public SpellCardBuilder addSpell(String firstName, String lastName, int... diffcult) {
		for (int di:diffcult) {
			sclist.add(new SpellCard(firstName, lastName, master, di));
		}
		return this;
	}

	public SpellCardBuilder addSpell(String name, int... diffcult) {
		for (int di:diffcult) {
			sclist.add(new SpellCard(name, master, di));
		}
		return this;
	}

	public String getSpell() {
		StringBuilder sb=new StringBuilder();
		for (SpellCard sc:sclist) {
			String tmp=null;
			switch (sc.diffcult) {
				case 0:
					tmp = "SpellCard.Easy";
					break;
				case 1:
					tmp = "SpellCard.Normal";
					break;
				case 2:
					tmp = "SpellCard.Hard";
					break;
				case 3:
					tmp = "SpellCard.Lunatic";
					break;
				case 4:
					tmp = "SpellCard.Extra";
					break;
				case 5:
					tmp = "SpellCard.Phantasm";
					break;
				case 6:
					tmp = "SpellCard.LastSpell";
					break;
				case 7:
					tmp = "SpellCard.Lastword";
					break;
				case 8:
					tmp = "SpellCard.Overdrive";
					break;
			}
			sb.append(String.format("new SpellCard(\"%s\", \"%s\", %s),\n", sc.name, sc.master, tmp));
		}
		return sb.toString();
	}
}
