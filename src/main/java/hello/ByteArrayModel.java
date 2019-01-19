package hello;

import java.util.Calendar;

public class ByteArrayModel {
	private long time;

    private byte[] data;

    public ByteArrayModel() {
    	this.setTime(Calendar.getInstance().getTimeInMillis());

        this.data = new byte[] {'t','e','s','t'};
    }

    public ByteArrayModel(byte[] data) {
    	this.setTime(Calendar.getInstance().getTimeInMillis());
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "";
	}
}
