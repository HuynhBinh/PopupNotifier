package tbstudio.cute.popup.notifier.model;

public class Sticker implements Comparable<Sticker>{
	
	private String packageName;
	private String appName;
	private Boolean isChecked;
	public Sticker(String packageName, String appName, Boolean isChecked) {
		super();
		this.packageName = packageName;
		this.appName = appName;
		this.isChecked = isChecked;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Boolean getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
	@Override
	public int compareTo(Sticker another) {
		
		int result = appName.compareTo(another.appName);
		
		return result;
	}
	
	
	
}
