package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import utils.Utils;
public class TestHash {

	@Test
	public void test() throws NoSuchAlgorithmException, IOException {
		String file = "/home/user0308/Tmp/tmp/A菌二维码.jpg";
		System.out.println(Utils.getFileSHA256Str(file));
		System.out.println(Utils.getFileSHA256Str(file));
		System.out.println(Utils.getFileSHA256Str(file));
		System.out.println(Utils.getFileSHA256Str(file));
	}

}
