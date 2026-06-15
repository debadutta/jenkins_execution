package jenkins_execution;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

public class TestBrokenLinks {
	
	private record LinkRecord(String link, String visibleText) {};
	@Test
	public void checkBrokenLinks() {
		ChromeOptions options=new ChromeOptions();
		options.addArguments("--headless=new");
		WebDriver driver = new ChromeDriver(options);
		System.out.println("*****************");
//		driver.get("https://www.eddymens.com/blog"
//				+ "/page-with-broken-pages-for-testing-53049e870421");
		driver.get("https://practice-automation.com/broken-links/");
		
		List<LinkRecord> linkRecords = new ArrayList<>();
		
		List<WebElement> listLinks = driver.findElements(By.tagName("a"));
		for(WebElement link:listLinks) {
			String actualURL=link.getAttribute("href");
			String visibleText=link.getText();
			
			if(actualURL==null || actualURL.isEmpty()) {
				System.out.println("The URL for UI link < " + visibleText+ " > is empty.");
				continue;
			}
			else if(actualURL.startsWith("javascript")||actualURL.startsWith("tel:")
						||actualURL.startsWith("mailto:")) {
				System.out.println("The URL for UI link < " + visibleText+ " > is "
						+ "either a javascript/email/telephone number.");
				continue;
			} else {
				linkRecords.add(new LinkRecord(actualURL,visibleText));
			}
				
		}
		
		for(LinkRecord linkRecord:linkRecords) {
			URI uri= URI.create(linkRecord.link());
			try {
				URL url=uri.toURL();
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setConnectTimeout(10000);
				connection.setRequestMethod("HEAD");
				connection.connect();
				int responseCode = connection.getResponseCode();
//				System.out.println(responseCode);
				if(responseCode >= 400)
					System.out.println("Link with visible text < "+linkRecord.visibleText()+
							" > with link < "+linkRecord.link()+" > is broken, "
									+ "returns status code : "+responseCode);
				connection.disconnect();
			} catch (IOException e) {
				System.out.println("--EXCEPTION:" + e.getMessage());
				System.out.println("--EXCEPTION:" + linkRecord.visibleText()+" "+
						linkRecord.link());
				
			}
			
		}
		driver.quit();
	}

}
