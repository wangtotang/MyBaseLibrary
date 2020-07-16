/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tang.util.security;

import android.util.Base64;

/**
 * Base64加解密
 *
 */
public class Base64Utils {
	
	/**
	 * Base64加密
	 * @param str
	 * @return
	 */
	public static String encode(String str){
		return new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
	}
	
	/**
	 * Base64解密
	 * @param strBase64
	 * @return
	 */
	public static String decode(String strBase64){
		return new String(Base64.decode(strBase64.getBytes(), Base64.DEFAULT));
	}

	/**
	 * Base64编码
	 *
	 * @param input 要编码的字符串
	 * @return Base64编码后的字符串
	 */
	public static byte[] base64Encode(String input) {
		return base64Encode(input.getBytes());
	}

	/**
	 * Base64编码
	 *
	 * @param input 要编码的字节数组
	 * @return Base64编码后的字符串
	 */
	public static byte[] base64Encode(byte[] input) {
		return Base64.encode(input, Base64.NO_WRAP);
	}

	/**
	 * Base64编码
	 *
	 * @param input 要编码的字节数组
	 * @return Base64编码后的字符串
	 */
	public static String base64Encode2String(byte[] input) {
		return Base64.encodeToString(input, Base64.NO_WRAP);
	}

	/**
	 * Base64解码
	 *
	 * @param input 要解码的字符串
	 * @return Base64解码后的字符串
	 */
	public static byte[] base64Decode(String input) {
		return Base64.decode(input, Base64.NO_WRAP);
	}

	/**
	 * Base64解码
	 *
	 * @param input 要解码的字符串
	 * @return Base64解码后的字符串
	 */
	public static byte[] base64Decode(byte[] input) {
		return Base64.decode(input, Base64.NO_WRAP);
	}

	/**
	 * Base64URL安全编码
	 * <p>将Base64中的URL非法字符�?,/=转为其他字符, 见RFC3548</p>
	 *
	 * @param input 要Base64URL安全编码的字符串
	 * @return Base64URL安全编码后的字符串
	 */
	public static byte[] base64UrlSafeEncode(String input) {
		return Base64.encode(input.getBytes(), Base64.URL_SAFE);
	}

}
