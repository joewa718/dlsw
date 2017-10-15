package com.dlsw.shopping;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
public class ShoppingApplicationTests {
	@Test
	public void contextLoads() throws IOException {
		//scale(比例)
		Thumbnails.of("C:\\Users\\joewa\\Desktop\\20131227172325812.png")
				.scale(0.25f)
				.toFile("C:\\Users\\joewa\\Desktop\\20131227172325812_25%.png");

		Thumbnails.of("C:\\Users\\joewa\\Desktop\\20131227172325812.png")
				.scale(1.10f)
				.toFile("C:\\Users\\joewa\\Desktop\\20131227172325812_110%.png");
		//keepAspectRatio(false)默认是按照比例缩放的
		Thumbnails.of("C:\\Users\\joewa\\Desktop\\20131227172325812.png")
				.size(200,200)
				.keepAspectRatio(true)
				.toFile("C:\\Users\\joewa\\Desktop\\a380_200x200.jpg");
	}

}
