package graduate.qk.com.mypush;

/**
 * Created by Administrator on 2017/10/29.
 */

public class ChatBean {
    public ChatBean(String results, boolean isAsk, int image) {
        this.results = results;
        this.isAsk = isAsk;
        this.image = image;
    }

    public String results;
    public boolean isAsk;
    public int image = -1;
}
