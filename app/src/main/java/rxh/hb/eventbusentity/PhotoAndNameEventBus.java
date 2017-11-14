package rxh.hb.eventbusentity;

import android.graphics.Bitmap;

public class PhotoAndNameEventBus {

	private String name;
	private String path;
	private Bitmap bitmap;

	public PhotoAndNameEventBus(String name, String path, Bitmap bitmap) {
		this.name = name;
		this.path = path;
		this.bitmap = bitmap;
	}

	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

}
