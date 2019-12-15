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

	public SpellCardBuilder addSpell(String name, int... diffcult) {
		int diffFlag=0;
		for (int di:diffcult) {	
			switch (di) {
				case 0:
					diffFlag |= SpellCard.Easy;
					break;
				case 1:
					diffFlag |= SpellCard.Normal;
					break;
				case 2:
					diffFlag |= SpellCard.Hard;
					break;
				case 3:
					diffFlag |= SpellCard.Lunatic;
					break;
				case 4:
					diffFlag |= SpellCard.Extra;
					break;
				case 5:
					diffFlag |= SpellCard.Phantasm;
					break;
				case 6:
					diffFlag |= SpellCard.LastSpell;
					break;
				case 7:
					diffFlag |= SpellCard.Lastword;
					break;
				case 8:
					diffFlag |= SpellCard.Overdrive;
					break;
			}	
		}
		sclist.add(new SpellCard(name, master, diffFlag));
		return this;
	}

	public String getSpell() {
		StringBuilder sb=new StringBuilder();
		for (SpellCard sc:sclist) {
			sb.append(String.format("new SpellCard(\"%s\", \"%s\", %s),\n", sc.name, sc.master, sc.diffcult));
		}
		return sb.toString();
	}
}
