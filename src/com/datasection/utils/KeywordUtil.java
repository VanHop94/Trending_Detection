package com.datasection.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.datasection.entities.Keyword;
import com.datasection.main.TrendDetection;

public class KeywordUtil {

	public static LinkedHashMap<String, Keyword> getKeyword(TrendDetection trendDetection, String fileName,
			List<String> datas, LinkedHashMap<String, LinkedHashMap<String, Keyword>> keywordData) {

		LinkedHashMap<String, Keyword> listKeyword = new LinkedHashMap<String, Keyword>();
		String time = fileName.substring(0, fileName.indexOf("."));
		for (String str : datas) {

			String[] tokens = str.split("\t");

			if (tokens.length != 3)
				continue;
			Keyword keyword = new Keyword(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), time);

			if (keyword.getQuantity() < 2)
				continue;

			listKeyword.put(keyword.getKeyword(), keyword);

			boolean isBursty = false;
			if (isBusrtyKeyword(trendDetection, keyword, keywordData)) {
				isBursty = true;
			}
			if (keywordData.size() == 0) {
				if (isBursty)
					keyword.addStatus(fileName, "true");
				else
					keyword.addStatus(fileName, "false");
			} else {

				for (String date : keywordData.keySet()) {
					LinkedHashMap<String, Keyword> keywordsTemp = keywordData.get(date);
					if (keywordsTemp == null)
						continue;
					Keyword keywordTemp = keywordsTemp.get(keyword.getKeyword());
					if (keywordTemp == null) {
						if (!keyword.getStatus()
								.contains(fileName + ":" + keyword.getQuantity() + ":" + keyword.getAmount() + ":true")
								&& !keyword.getStatus().contains(fileName + ":" + keyword.getQuantity() + ":"
										+ keyword.getAmount() + ":false")) {
							if (isBursty)
								keyword.addStatus(fileName, "true");
							else
								keyword.addStatus(fileName, "false");
						}
					} else {
						keyword.setStatus(keywordTemp.getStatus());
						if (!keyword.getStatus()
								.contains(fileName + ":" + keyword.getQuantity() + ":" + keyword.getAmount() + ":true")
								&& !keyword.getStatus().contains(fileName + ":" + keyword.getQuantity() + ":"
										+ keyword.getAmount() + ":false")) {
							if (isBursty)
								keyword.addStatus(fileName, "true");
							else
								keyword.addStatus(fileName, "false");
						}
					}
				}
			}
			
			if(isBursty && isBurstyKeyword(keyword.getStatus()))
				trendDetection.getBurstyKeywords().add(keyword);

		}

		return listKeyword;
	}

	public static boolean isBusrtyKeyword(TrendDetection trendDetection, Keyword keyword1,
			LinkedHashMap<String, LinkedHashMap<String, Keyword>> keywordData) {

		/*
		 * Ratio(K,t) = [TF_k(t) - TF_k(t-1)] / TF_k(t) K = keyword , t = time
		 */

		Float w = trendDetection.getListPopularKeyword().get(keyword1.getKeyword());
		Keyword keyword2 = null;
		try {
			keyword2 = keywordData.get(DateUtil.getLastTime(keyword1.getTime())).get(keyword1.getKeyword());
		} catch (Exception e) {

			if (keyword1.getQuantity() > (ArgsConstant.newInstance().NORMAL_MIN * 2 / 3) && w == null)
				return true;

			return false;
		}

		if (keyword2 == null) {

			if (w != null) {
				if (keyword1.getQuantity() > (w * 2))
					return true;
				return false;
			}

			if (keyword1.getQuantity() > ArgsConstant.newInstance().NORMAL_MIN)
				return true;

			if (DateUtil.isMiddleNight(keyword1.getTime()))
				if (keyword1.getQuantity() > ArgsConstant.newInstance().MIDDLE_NIGHT_MIN)
					return true;

			return false;
		}

		if (keyword1.getQuantity() < ArgsConstant.newInstance().NORMAL_MIN)
			return false;

		float ratio = ((float) (keyword1.getQuantity() - keyword2.getQuantity())) / keyword1.getQuantity();
		
		if (w == null && ratio >= ArgsConstant.newInstance().NORMAL_RATIO)
			return true;
		if (w != null && ratio >= ArgsConstant.newInstance().MIDDLE_NIGHT_RATIO && (keyword1.getQuantity() > (w * 2)))
			return true;
		if (w != null && ratio >= ArgsConstant.newInstance().MIDDLE_NIGHT_RATIO && (keyword1.getQuantity() < (w * 2)))
			return false;

		/* middle night */
		if (DateUtil.isMiddleNight(keyword1.getTime())) {
			float k1 = keyword1.getQuantity() / keyword1.getAmount();
			float k2 = keyword2.getQuantity() / keyword2.getAmount();
			ratio = (k1 - k2) / k1;
			if (ratio >= ArgsConstant.newInstance().NORMAL_RATIO
					&& keyword1.getQuantity() >= ArgsConstant.newInstance().MIDDLE_NIGHT_MIN)
				return true;
		}

		return false;
	}

	private static boolean isBurstyKeyword(ArrayList<String> status) {

		if (status.size() == 0)
			return true;

		for (int i = 0 ; i < status.size() - 1 ; i++) {
			String st = status.get(i);
			String[] tokens = st.split(":");
			if (tokens[3].equals("true"))
				return false;

		}

		return true;
	}

}
