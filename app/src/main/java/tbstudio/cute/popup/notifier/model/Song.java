package tbstudio.cute.popup.notifier.model;

public class Song implements Comparable<Song> {
	private long id;
	private String title;
	private boolean isPlay;
	private boolean isIntenal;

	public Song(long id, String title, boolean isPlay, boolean isIntenal) {
		super();
		this.id = id;
		this.title = title;
		this.isPlay = isPlay;
		this.isIntenal = isIntenal;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isPlay() {
		return isPlay;
	}

	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}

	@Override
	public int compareTo(Song another) {
		return this.title.compareTo(another.getTitle());
	}

	public boolean isIntenal() {
		return isIntenal;
	}

	public void setIntenal(boolean isIntenal) {
		this.isIntenal = isIntenal;
	}

}
