package com.jiutong.utils.lang;

import java.util.HashMap;

public class SNID {

		/**
		 * 加密规则：5位UID + 10位随机符 + 1位标记位（mapIndex） + 2位EMAIL长度 （16进制）+
		 * 16进制EMAIL，最后补足128位
		 **/
	    public static String encode(int uid, String createDT, long lastActive) {
			int mapIndex = (int) (lastActive % 16);
			char[] cuid = encodeUid(mapIndex, uid);

			mapIndex = 15 - mapIndex;
			char cmail[] = encodeEmail(mapIndex, createDT);

			int ods[] = arrange[mapIndex];
			char ret[] = new char[128];
			for (int i = 0; i < ret.length; i++) {
				if (i == 15)
					ret[15] = base64_char.charAt(mapIndex);
				else
					ret[ods[i]] = i < 15 ? cuid[i] : cmail[i - 16];
			}

			return new String(ret);

		}

		public static String[] decode(String src) {

			if (StringUtils.isEmptyString(src) || src.length() != 128)
				throw new IllegalArgumentException(
						"Can't decode, caused by the wrong input source string: "
								+ src);

			int mapIndex = getV64(src.charAt(15));
			String ret[] = new String[3];
			ret[0] = Integer.toString(decodeUID(mapIndex, src));
			ret[1] = decodeEmail(mapIndex, src);
			ret[2] = Integer.toString(mapIndex);

			return ret;
		}

		private static char[] encodeUid(int mapIndex, int uid) {

			/** 5位有效,加10位随机码 **/
			String comp = CommonUtils.getRandomString(10);

			char cuid[] = { base64_char.charAt(uid >> 24 & 63),
					base64_char.charAt(uid >> 18 & 63),
					base64_char.charAt(uid >> 12 & 63),
					base64_char.charAt(uid >> 6 & 63),
					base64_char.charAt(uid & 63),
					base64_char.charAt(comp.charAt(0) & 63),
					base64_char.charAt(comp.charAt(1) & 63),
					base64_char.charAt(comp.charAt(2) & 63),
					base64_char.charAt(comp.charAt(3) & 63),
					base64_char.charAt(comp.charAt(4) & 63),
					base64_char.charAt(comp.charAt(5) & 63),
					base64_char.charAt(comp.charAt(6) & 63),
					base64_char.charAt(comp.charAt(7) & 63),
					base64_char.charAt(comp.charAt(8) & 63),
					base64_char.charAt(comp.charAt(9) & 63) };

			for (int i = 0; i < cuid.length; i++)
				cuid[i] = getEncodeChar(mapIndex, cuid[i]);

			return cuid;
		}

		public static int decodeUID(int mapIndex, String src) {

			int ods[] = arrange[mapIndex];
			char val[] = new char[] { src.charAt(ods[0]), src.charAt(ods[1]),
					src.charAt(ods[2]), src.charAt(ods[3]), src.charAt(ods[4]) };

			mapIndex = 15 - mapIndex;
			for (int i = 0; i < val.length; i++)
				val[i] = getDecodeChar(mapIndex, val[i]);

			int ret = 0;
			for (int i = 0; i < val.length; i++) {
				ret <<= 6;
				ret += (getV64(val[i]) & 63);
			}

			return ret;
		}

		private static char[] encodeEmail(int mapIndex, String email) {

			char[] mail = new char[email.length() * 2];
			for (int i = 0, j = 0; i < email.length(); j = (++i) * 2) {
				char[] tmp = toHexChar(email.charAt(i));
				mail[j] = tmp[0];
				mail[j + 1] = tmp[1];
			}

			/** 15位记录UID, 1位记录mapIndex **/
			char ret[] = new char[128 - 15 - 1];

			/** 2位记录长度 **/
			char[] mailLength = toHexChar(mail.length);
			ret[0] = mailLength[0];
			ret[1] = mailLength[1];

			String comp = CommonUtils.getRandomString(ret.length - mail.length);
			for (int i = 2; i < ret.length; i++) {
				/** 余位记录EMAIL,不足用随机字符补全 **/
				ret[i] = base64_char
						.charAt(i - 1 > mail.length ? (comp.charAt(i - 2
								- mail.length) & 63) : (mail[i - 2] & 63));
			}

			for (int i = 0; i < ret.length - 1; i++)
				ret[i] = getEncodeChar(mapIndex, ret[i]);

			return ret;
		}

		private static String decodeEmail(int mapIndex, String src) {

			int ods[] = arrange[mapIndex];
			String hexLength = parseHexChar(new char[] {
					getDecodeChar(mapIndex, src.charAt(ods[16])),
					getDecodeChar(mapIndex, src.charAt(ods[17])) });

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < Integer.parseInt(hexLength, 16) / 2; i++)
				sb.append((char) Integer.parseInt(
						parseHexChar(new char[] {
								(char) (getV64(getDecodeChar(mapIndex,
										src.charAt(ods[i * 2 + 18]))) | 64),
								(char) (getV64(getDecodeChar(mapIndex,
										src.charAt(ods[i * 2 + 19]))) | 64) }),
						16));

			return sb.toString();
		}

		private static char getEncodeChar(int mapIndex, char src) {
			return ((Character) enMap[mapIndex].get(new Character(src)))
					.charValue();
		}

		private static char getDecodeChar(int mapIndex, char src) {
			return ((Character) deMap[mapIndex].get(new Character(src)))
					.charValue();
		}

		private static int getV64(char src) {
			return ((Integer) val64.get(new Character(src))).intValue();
		}

		private static char[] toHexChar(int i) {
			char[] ret = new char[2];
			String tmp = Integer.toHexString(i & 0XFF).toUpperCase();
			ret[0] = base64_char.charAt(tmp.charAt(0) & 63);
			ret[1] = base64_char.charAt(tmp.charAt(1) & 63);
			return ret;
		}

		private static String parseHexChar(char[] c) {
			if (c == null || c.length != 2)
				throw new IllegalArgumentException(
						"The input char[]'s length must be 2!");

			c[0] = (char) (getV64(c[0]) < 48 ? (getV64(c[0])) | 64
					: (getV64(c[0])) & 63);

			c[1] = (char) (getV64(c[1]) < 48 ? (getV64(c[1])) | 64
					: (getV64(c[1])) & 63);

			return new String(c);
		}

		private static final String base64_char = "ABCDEFGHIJKLMNOPQRSTUVWXYZ+1234567890/abcdefghijklmnopqrstuvwxyz";

		@SuppressWarnings("unchecked")
		private static HashMap<Character, Character>[] enMap = new HashMap[32];

		@SuppressWarnings("unchecked")
		private static HashMap<Character, Character>[] deMap = new HashMap[32];

		private static HashMap<Character, Integer> val64 = new HashMap<Character, Integer>();

		private static final int arrange[][] = {
				{ 63, 33, 9, 121, 38, 90, 10, 79, 2, 18, 85, 74, 81, 23, 52,
						15, 73, 26, 11, 92, 78, 48, 104, 21, 17, 7, 44, 126,
						62, 87, 58, 20, 27, 108, 127, 88, 99, 64, 102, 60, 36,
						71, 65, 49, 97, 115, 54, 28, 122, 19, 77, 55, 80, 72,
						47, 40, 43, 31, 1, 46, 82, 8, 96, 112, 5, 123, 125,
						118, 124, 37, 0, 107, 22, 57, 75, 76, 12, 106, 93, 35,
						14, 116, 109, 98, 24, 51, 39, 16, 89, 45, 59, 50, 70,
						3, 61, 100, 120, 86, 30, 29, 117, 4, 6, 25, 95, 13,
						114, 113, 103, 42, 110, 105, 66, 56, 119, 68, 101, 67,
						111, 83, 32, 53, 69, 34, 94, 41, 84, 91 },
				{ 82, 118, 112, 16, 79, 48, 83, 35, 70, 107, 30, 74, 32, 43,
						66, 15, 109, 117, 125, 67, 53, 3, 97, 75, 126, 110,
						114, 123, 1, 98, 20, 61, 60, 27, 26, 31, 29, 7, 124,
						24, 100, 94, 57, 103, 14, 77, 52, 6, 51, 5, 71, 86, 58,
						45, 68, 73, 13, 9, 106, 36, 49, 18, 17, 4, 92, 39, 93,
						44, 64, 88, 95, 65, 81, 56, 120, 63, 62, 55, 12, 72,
						59, 76, 115, 102, 0, 116, 8, 50, 28, 11, 19, 89, 69,
						33, 101, 90, 34, 37, 23, 47, 84, 38, 96, 99, 40, 108,
						54, 46, 105, 22, 21, 127, 2, 78, 41, 122, 111, 121,
						104, 80, 10, 113, 91, 25, 87, 119, 42, 85 },
				{ 118, 112, 49, 115, 97, 124, 83, 126, 121, 101, 99, 8, 69,
						113, 28, 15, 127, 103, 32, 26, 55, 91, 6, 60, 42, 114,
						2, 72, 7, 120, 84, 59, 77, 5, 75, 13, 54, 20, 14, 23,
						98, 62, 65, 104, 79, 47, 3, 16, 63, 56, 81, 58, 64,
						110, 44, 119, 85, 25, 122, 40, 33, 125, 1, 12, 68, 22,
						50, 92, 87, 107, 89, 41, 78, 0, 100, 86, 38, 17, 82,
						93, 29, 57, 106, 31, 48, 102, 76, 96, 18, 116, 73, 27,
						24, 46, 9, 36, 109, 90, 94, 61, 39, 117, 53, 45, 111,
						30, 4, 71, 52, 51, 21, 19, 74, 123, 88, 37, 105, 67,
						70, 43, 108, 11, 95, 10, 35, 80, 66, 34 },
				{ 110, 37, 9, 95, 47, 30, 31, 81, 48, 96, 109, 104, 82, 6, 24,
						15, 50, 99, 111, 68, 89, 32, 113, 127, 22, 76, 0, 8,
						126, 25, 112, 49, 83, 103, 116, 90, 102, 38, 115, 101,
						27, 63, 11, 59, 105, 118, 34, 106, 123, 75, 41, 85, 97,
						86, 3, 7, 12, 92, 52, 67, 74, 91, 33, 2, 19, 94, 23,
						87, 114, 65, 57, 20, 36, 39, 122, 26, 60, 125, 72, 43,
						73, 84, 98, 55, 62, 107, 13, 4, 29, 108, 77, 35, 93, 1,
						56, 78, 119, 5, 80, 21, 117, 79, 100, 44, 71, 51, 14,
						69, 46, 10, 121, 18, 66, 16, 88, 28, 40, 42, 61, 64,
						124, 58, 70, 53, 54, 45, 120, 17 },
				{ 107, 33, 68, 82, 54, 63, 72, 98, 41, 48, 20, 120, 43, 108, 0,
						15, 96, 112, 121, 81, 49, 10, 77, 71, 92, 44, 37, 97,
						61, 115, 34, 57, 47, 78, 26, 32, 91, 14, 79, 64, 55,
						39, 2, 36, 19, 3, 56, 59, 88, 90, 103, 5, 16, 9, 11,
						124, 69, 99, 125, 24, 110, 127, 109, 58, 13, 117, 126,
						75, 30, 28, 119, 8, 40, 50, 1, 12, 65, 67, 86, 105,
						101, 84, 25, 46, 4, 27, 35, 17, 53, 7, 45, 114, 22, 94,
						23, 95, 83, 93, 6, 29, 113, 70, 87, 60, 73, 102, 111,
						122, 106, 116, 76, 42, 123, 51, 118, 85, 89, 74, 62,
						52, 66, 31, 104, 21, 18, 80, 38, 100 },
				{ 106, 36, 60, 72, 93, 61, 96, 44, 34, 37, 122, 97, 52, 3, 42,
						15, 117, 127, 80, 40, 35, 92, 67, 70, 54, 87, 49, 104,
						20, 108, 51, 0, 71, 13, 56, 110, 101, 10, 79, 4, 17,
						59, 57, 84, 32, 41, 77, 68, 120, 66, 26, 98, 126, 14,
						30, 18, 27, 2, 73, 11, 113, 88, 105, 115, 69, 91, 114,
						1, 28, 86, 63, 124, 21, 123, 9, 109, 55, 22, 90, 78,
						116, 95, 62, 81, 43, 100, 7, 46, 89, 24, 5, 107, 112,
						19, 76, 47, 99, 33, 53, 119, 8, 121, 39, 23, 58, 85,
						45, 82, 125, 6, 38, 111, 74, 94, 25, 48, 16, 103, 12,
						102, 118, 50, 29, 65, 31, 75, 83, 64 },
				{ 102, 30, 40, 53, 11, 110, 16, 99, 13, 58, 100, 73, 45, 25,
						14, 15, 17, 92, 4, 26, 0, 71, 20, 108, 29, 44, 41, 22,
						38, 63, 95, 78, 5, 48, 69, 59, 104, 98, 107, 70, 54,
						52, 74, 106, 42, 122, 124, 87, 72, 61, 23, 97, 2, 50,
						27, 35, 86, 8, 33, 19, 81, 119, 57, 113, 83, 91, 80,
						43, 121, 125, 111, 9, 36, 28, 76, 120, 65, 31, 6, 93,
						62, 7, 56, 18, 51, 21, 49, 112, 39, 109, 3, 66, 64,
						101, 32, 37, 55, 114, 77, 116, 1, 88, 12, 67, 115, 103,
						117, 60, 46, 34, 127, 105, 24, 90, 89, 96, 126, 94, 82,
						10, 85, 79, 84, 75, 123, 68, 47, 118 },
				{ 116, 5, 47, 42, 80, 43, 62, 1, 6, 44, 78, 56, 113, 101, 79,
						15, 37, 96, 72, 105, 103, 46, 57, 10, 52, 49, 119, 31,
						14, 75, 92, 127, 91, 123, 20, 102, 30, 84, 26, 35, 115,
						22, 89, 51, 61, 87, 77, 54, 67, 9, 23, 90, 29, 125, 38,
						24, 41, 112, 104, 59, 76, 82, 85, 94, 118, 88, 25, 74,
						70, 114, 7, 64, 93, 97, 66, 58, 122, 36, 63, 3, 65,
						107, 55, 32, 50, 86, 100, 11, 121, 39, 98, 45, 81, 117,
						99, 40, 120, 60, 110, 19, 73, 16, 124, 18, 95, 109, 2,
						68, 21, 28, 27, 0, 13, 108, 53, 33, 12, 4, 34, 17, 8,
						111, 48, 126, 69, 83, 106, 71 },
				{ 92, 50, 21, 89, 74, 8, 111, 39, 64, 98, 22, 31, 77, 71, 107,
						15, 124, 78, 2, 100, 33, 103, 123, 75, 113, 97, 73,
						115, 59, 47, 49, 72, 67, 6, 102, 38, 81, 23, 54, 32, 4,
						46, 120, 61, 105, 114, 95, 87, 86, 28, 104, 13, 45, 58,
						9, 44, 125, 63, 121, 84, 34, 119, 82, 68, 40, 76, 11,
						42, 20, 94, 25, 37, 110, 101, 109, 96, 60, 35, 80, 12,
						7, 83, 85, 36, 52, 19, 1, 62, 26, 70, 122, 88, 65, 53,
						27, 51, 90, 117, 30, 43, 41, 118, 106, 93, 16, 57, 66,
						0, 17, 126, 55, 3, 48, 24, 14, 10, 5, 112, 56, 69, 116,
						18, 108, 127, 79, 91, 99, 29 },
				{ 55, 87, 80, 97, 53, 56, 107, 41, 83, 32, 48, 111, 7, 104,
						112, 15, 35, 8, 9, 100, 126, 113, 98, 44, 34, 88, 23,
						119, 61, 21, 5, 6, 68, 62, 42, 94, 99, 70, 121, 1, 66,
						91, 74, 16, 22, 86, 110, 118, 84, 12, 78, 77, 75, 50,
						67, 64, 18, 93, 76, 103, 73, 11, 29, 117, 57, 3, 10,
						89, 105, 90, 36, 101, 24, 46, 0, 4, 43, 115, 19, 51,
						52, 38, 60, 63, 59, 123, 102, 114, 26, 20, 108, 37, 2,
						79, 116, 58, 45, 17, 13, 81, 125, 95, 30, 54, 28, 82,
						25, 96, 31, 72, 40, 14, 122, 120, 92, 109, 127, 33, 49,
						27, 106, 71, 65, 39, 47, 85, 124, 69 },
				{ 99, 43, 87, 20, 95, 121, 13, 75, 32, 100, 79, 58, 66, 60, 49,
						15, 80, 86, 1, 77, 28, 2, 34, 84, 8, 11, 16, 36, 96,
						22, 21, 113, 45, 94, 64, 57, 102, 69, 88, 18, 33, 112,
						116, 40, 46, 104, 39, 29, 125, 44, 4, 122, 24, 115, 14,
						123, 48, 71, 27, 9, 110, 19, 103, 10, 41, 74, 37, 7,
						30, 89, 114, 67, 107, 50, 83, 120, 82, 56, 81, 62, 47,
						127, 26, 63, 109, 97, 72, 38, 85, 12, 55, 111, 35, 51,
						78, 5, 53, 31, 90, 68, 59, 105, 118, 23, 98, 124, 25,
						73, 54, 6, 101, 92, 52, 76, 17, 0, 119, 91, 117, 70,
						126, 93, 106, 108, 42, 3, 61, 65 },
				{ 25, 62, 54, 117, 2, 78, 12, 99, 0, 107, 112, 106, 45, 80, 44,
						15, 101, 119, 127, 74, 8, 103, 88, 63, 16, 39, 3, 18,
						115, 111, 21, 69, 22, 7, 87, 5, 65, 104, 59, 93, 19,
						77, 20, 29, 66, 51, 32, 114, 97, 100, 40, 71, 28, 75,
						94, 82, 67, 6, 83, 113, 72, 125, 96, 41, 60, 70, 123,
						48, 14, 95, 89, 17, 122, 90, 24, 53, 38, 64, 116, 33,
						46, 108, 42, 73, 52, 81, 57, 55, 1, 47, 85, 11, 61, 92,
						98, 126, 27, 49, 23, 26, 68, 110, 76, 35, 10, 124, 34,
						86, 30, 43, 91, 37, 31, 13, 109, 105, 118, 120, 4, 102,
						58, 36, 84, 56, 121, 79, 50, 9 },
				{ 53, 66, 40, 26, 31, 111, 22, 13, 75, 60, 2, 41, 91, 110, 121,
						15, 70, 24, 93, 74, 112, 17, 79, 42, 64, 78, 32, 68,
						109, 84, 50, 124, 39, 37, 113, 104, 8, 44, 81, 105,
						127, 14, 83, 5, 62, 65, 28, 76, 120, 114, 9, 82, 116,
						86, 51, 59, 80, 36, 43, 20, 11, 16, 27, 55, 46, 35, 72,
						30, 125, 45, 21, 19, 117, 107, 63, 7, 92, 56, 101, 100,
						77, 4, 69, 88, 10, 33, 52, 90, 94, 115, 57, 118, 1, 34,
						103, 58, 25, 12, 98, 23, 122, 18, 87, 123, 85, 73, 49,
						3, 95, 0, 96, 97, 71, 67, 126, 99, 54, 106, 119, 89,
						108, 6, 47, 29, 38, 61, 102, 48 },
				{ 115, 13, 70, 98, 2, 45, 102, 21, 32, 96, 94, 24, 25, 64, 5,
						15, 72, 111, 14, 47, 80, 124, 51, 88, 43, 33, 6, 61,
						97, 55, 28, 63, 105, 93, 79, 99, 120, 101, 42, 31, 118,
						10, 127, 60, 123, 83, 112, 66, 30, 114, 106, 35, 104,
						108, 19, 49, 103, 69, 4, 17, 89, 40, 85, 126, 122, 84,
						56, 9, 92, 67, 73, 44, 116, 109, 113, 41, 54, 90, 37,
						20, 71, 36, 52, 86, 117, 1, 81, 78, 29, 77, 57, 107,
						18, 11, 3, 82, 75, 121, 76, 38, 125, 62, 59, 53, 110,
						91, 58, 8, 65, 23, 46, 50, 27, 12, 26, 22, 100, 48, 34,
						68, 39, 95, 74, 7, 16, 0, 87, 119 },
				{ 115, 77, 30, 49, 13, 80, 75, 61, 73, 90, 109, 76, 34, 11, 10,
						15, 5, 35, 60, 74, 100, 54, 81, 58, 98, 69, 95, 9, 117,
						85, 50, 93, 113, 25, 114, 102, 63, 119, 18, 79, 55, 1,
						56, 39, 104, 67, 42, 3, 22, 71, 88, 45, 16, 8, 127, 78,
						123, 72, 38, 70, 43, 33, 12, 23, 26, 122, 27, 19, 7,
						24, 84, 14, 91, 99, 101, 124, 62, 59, 51, 83, 116, 17,
						46, 66, 2, 21, 68, 48, 118, 44, 37, 105, 97, 64, 110,
						57, 94, 87, 41, 92, 126, 4, 111, 107, 120, 31, 36, 47,
						52, 103, 53, 65, 112, 28, 32, 125, 20, 89, 29, 82, 106,
						108, 86, 6, 121, 96, 40, 0 },
				{ 106, 6, 29, 33, 50, 71, 17, 76, 102, 108, 21, 103, 27, 121,
						2, 15, 44, 52, 100, 19, 16, 65, 94, 80, 74, 31, 114,
						49, 116, 125, 104, 79, 95, 85, 4, 105, 118, 13, 83, 48,
						5, 70, 107, 7, 25, 122, 96, 97, 26, 63, 28, 18, 58, 67,
						91, 84, 8, 78, 126, 10, 69, 77, 109, 34, 93, 22, 111,
						9, 12, 89, 43, 68, 101, 20, 75, 36, 38, 113, 1, 87, 82,
						57, 90, 3, 39, 124, 73, 123, 24, 110, 0, 37, 59, 99,
						23, 119, 127, 62, 115, 41, 56, 32, 55, 14, 47, 61, 88,
						60, 92, 11, 54, 98, 53, 40, 51, 45, 46, 66, 112, 35,
						64, 117, 30, 42, 72, 81, 120, 86 } };

		private static final String keys[] = {
				"WhuyKqMEmixcH/p6n32stJo7GwATdBPUrRD8fQlOC1kgeS4Iv0bLXa59YFj+ZNzV",
				"I4yqDm/cEQfCh3inoBLORtGZSgJM5de8uY69vx+2HFbWUX7swzrj0aVTlkN1pPAK",
				"rqzcHD7JiKSGWgZjwToEVtINRdbX3sPuBUhnMOx+m619Fv248la/e5kYCQL0yfAp",
				"gGJeA1ur5Lkcw8dKB0OxN+/ZQPnEzbUXHFW3YSypish6fT4ovmMjVRID7tl2qCa9",
				"gdBR6t7Dif/rzqT30ysZuJOWpnC4hPkcbS9l2Xw1oMA5KYLGHjVvFQEN+ex8UmaI",
				"4biYxkGPZ/9NFOWyCBUtHlewTRjQMLKXS+pJ13ADmr5na6VdE7gu0vIsczo2hf8q",
				"gfJk8wHsd/vESYOc3Pu1nKjrMqoBtGiplea4+0bA6zyLNhRQF72UxVZXW5ITmD9C",
				"Z4eEnBaUvrDl/p6hWAx7uc5G3ISdyM9CY1fVi08wTzQXqjtNHJF2sLmkKOo+gbPR",
				"P3Gf54zmD82KC/+VkwiOZ1X9YLFIaERnNgdAUybslB0utWTMhoJS7Hqp6Qcrvjxe",
				"njUzo3KSDEh0WGYa2XvbCu5irwFyLmpTkVZNqOtlx78BJs4dR+APg1fe/cMHI96Q",
				"JkH6+CSZLQxFUcTr0M2g4vsBiKjqwDa3RtOP/nb5W1uldXzIfEAN9yY7Vp8ohmeG",
				"izmKoEk2NvMprPBcDwl156uOgQ+SUHeLJWa4ItFCZ73dThyRAXGqj/0s8fxYnV9b",
				"rKp71aOXIAnxsmzwPv2oW4JDyEjG+6YUlbCMkQecdB9q8V5Lu0gRNfT/HStFiZ3h",
				"rO3e1QKMljF28xL4D6VPpbGhNm+oRwZ5WCnkIvdBuT/Jg0aciXHEAsfYt97UySqz",
				"l7MoBDvx2Cw0ZmKP96+fL5tj4dXTgJs8U/eOHzRuQi13IYqcVpaGAErbyNFkhSWn",
				"YXmEo5lG/VPbJduM2U+qC4fDgp0SK3rR1QztjvIswcTH9kFOZiL7WNA68nByaxeh" };

	static {
			for (int m = 0; m < keys.length; m++) {
				enMap[m] = new HashMap<Character, Character>();
				deMap[m] = new HashMap<Character, Character>();
				for (int i = 0; i < base64_char.length(); i++) {
					enMap[m].put(new Character(base64_char.charAt(i)),
							new Character(keys[m].charAt(i)));
					deMap[m].put(new Character(keys[m].charAt(i)),
							new Character(base64_char.charAt(i)));
				}

			}

			for (int i = 0; i < base64_char.length(); i++)
				val64.put(new Character(base64_char.charAt(i)), new Integer(i));

	}

	public static void main(String args[]) {
		long dt = System.currentTimeMillis();
		System.out.println(dt);
		String snid = SNID.encode(101166, dt+"",
				System.currentTimeMillis());
		System.out.println(snid);

		String ss[] = SNID.decode(snid);
		System.out.println(ss[0]);
		System.out.println(ss[1]);
	}
}