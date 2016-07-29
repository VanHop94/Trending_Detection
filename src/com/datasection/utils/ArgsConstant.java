package com.datasection.utils;

public class ArgsConstant {

	public float NORMAL_RATIO;
	public int NORMAL_MIN;

	public float MIDDLE_NIGHT_RATIO;
	public int MIDDLE_NIGHT_MIN;

	public String DATA_FOLDER;
	public String OUTPUT;

	public int TIME_SLEEP;

	public Thread t;
	
	private static ArgsConstant args;

	public static ArgsConstant newInstance(){
		
		if(args == null)
			args = new ArgsConstant();
		
		return args;
		
	}
	
	private ArgsConstant() {

		t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						NORMAL_RATIO = Float.parseFloat(FileUtils.getProperties().getProperty("NORMAL_RATIO"));
						NORMAL_MIN = Integer.parseInt(FileUtils.getProperties().getProperty("NORMAL_MIN"));

						MIDDLE_NIGHT_RATIO = Float
								.parseFloat(FileUtils.getProperties().getProperty("MIDDLE_NIGHT_RATIO"));
						MIDDLE_NIGHT_MIN = Integer.parseInt(FileUtils.getProperties().getProperty("MIDDLE_NIGHT_MIN"));

						DATA_FOLDER = FileUtils.getProperties().getProperty("DATA_FOLDER");
						OUTPUT = FileUtils.getProperties().getProperty("OUTPUT");

						TIME_SLEEP = Integer.parseInt(FileUtils.getProperties().getProperty("SLEEP_TIME"));

						Thread.sleep(1000 * 5);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();

	}

}
