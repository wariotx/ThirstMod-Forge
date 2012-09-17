package tarun1998.thirstmod.api;

public interface IContentAPI extends IAPIBase {
	
	/**
	 * Called when a file is been read from the Custom Drink Creation.
	 * @param fileName the name of the file being read.
	 * @param split line split for reading. Look at ContentLoader.read on how
	 * this works.
	 * @param type what class is calling this.
	 */
	public void onReadFile(String fileName, String[] split, Class type);
}
