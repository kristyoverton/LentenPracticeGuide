package whiletrue.ado.marionberryjam;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;


public class Practice {
    private String id;
    private PracticeTypes type;
    private String verse;
    private String body;
    private String teaser;
    private String subteaser;
    private String bodyTitle;
    private String day;
    private String link;

    Practice(JSONObject json) {

        try {
            type = PracticeTypes.valueOf(json.getString("type"));
            teaser = json.getString("teaser");
            verse = json.optString("verse", "");
            body = json.optString("body","");
            id = json.getString("id");
            day = json.optString("day", "");
            subteaser = json.optString("subteaser", "");
            link = json.optString("link", "");
        }
        catch (JSONException e){

        }
    }

    public String getId() {
        return this.id;
    }

    public PracticeTypes getType() {
        return this.type;
    }

    public String getVerse() {
        return this.verse;
    }

    public String getTeaser() {
        return this.teaser;
    }
    public String getSubteaser() {
        return this.subteaser;
    }

    public String getbody() {
        return this.body;
    }

    public String getBodyTitle() {return this.bodyTitle;}

    public String getDay() {return this.day == null? "" : this.day;}

    public String getLink() {return this.link;}

}
