package dataliciousAssignment;

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;

public class NewPhantomJsTest {
	public WebDriver driver;
	public BrowserMobProxy proxy;
	String urlQueryString;
	Boolean analyticsCheck = false, optimahubCheck = false;
	List<WebElement> links;
	
@Test
public void task1() throws Exception {
	
	System.out.println("In Task 1()");
	
	driver.get("http://www.google.com");
	
	driver.manage().window().maximize();
	
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
	System.out.println("Page title is: " + driver.getTitle());
	
	WebElement element = driver.findElement(By.name("q"));	
    
	Assert.assertTrue(element.isDisplayed(), "Search text box did not appear!");
	
	String searchText = "Datalicious";
	
	element.sendKeys(searchText);					
    
	Thread.sleep(2000);
	
	driver.findElement(By.xpath("//input[@value='Google Search']")).click();
	
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
	System.out.println("Page title is: " + driver.getTitle());
    
	String searchTitle = searchText.concat(" - Google Search");
    
    Assert.assertTrue(driver.getTitle().equals(searchTitle), "Search happened for some other text other than '"+searchText+"'");
	
    Thread.sleep(2000);
    
    //WebDriverWait wait1 = new WebDriverWait(driver, 10, 500);
    //wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='rcnt']")));
    
    /*wait1 = new WebDriverWait(driver, 45, 500);
    wait1.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='rso']/div/div/div/div/div/h3/a")));
    */
    List<WebElement> noOfOrganicResults = driver.findElements(By.partialLinkText("Datalicious"));
    
    //List<WebElement> noOfOrganicResults = driver.findElements(By.xpath(".//*[@id='rso']/div/div/div/div/div/h3/a"));
    
    Thread.sleep(2000);
    
    System.out.println("No. of Organic Results ="+noOfOrganicResults.size());
    
    Iterator<WebElement> linksToClicks = noOfOrganicResults.iterator();
    
    while(linksToClicks.hasNext())
    {
    	//linksToClicks.next().click();
    	System.out.println("noOfOrganicResults.get(0)="+noOfOrganicResults.get(0).getTagName());
    	System.out.println("noOfOrganicResults.get(0)="+noOfOrganicResults.get(0).getAttribute("href"));
    	//noOfOrganicResults.get(0).click();
    	
    	/*JavascriptExecutor js = (JavascriptExecutor)driver;
    	js.executeScript("arguments[0].click();", noOfOrganicResults.get(0));*/
    	
    	driver.get(noOfOrganicResults.get(0).getAttribute("href"));
    	
    	/*String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject){ arguments[0].fireEvent('onmouseover');}";
    	String onClickScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('click',true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject){ arguments[0].fireEvent('onclick');}";

    	js.executeScript(mouseOverScript, noOfOrganicResults.get(0));
    	js.executeScript(onClickScript, noOfOrganicResults.get(0));*/
    	
    	/*Actions a = new Actions(driver);
    	Action b = a.moveToElement(noOfOrganicResults.get(0)).sendKeys(Keys.ENTER).build();
    	b.perform();*/
    	
    	driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    	
    	Thread.sleep(5000);
    	/*WebDriverWait wait = new WebDriverWait(driver, 10, 500);
    	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("siteInfo")));*/
    	
    	String siteURL = driver.getCurrentUrl();
    	System.out.println("site url="+siteURL);
    	Assert.assertEquals( siteURL , "https://www.datalicious.com/", "Incorrect URL opened!");
    	
    	String siteTitle = driver.getTitle();
    	System.out.println("site url="+siteTitle);
    	Assert.assertEquals( siteTitle , "Marketing Data Specialists | Datalicious", "Incorrect Page opened and hence Incorrect Page Title");
    	
    	break;
    }
    
}

@Test(dependsOnMethods = "task1")
public void task2() throws Exception{
	
	System.out.println("In Task 2()");
	driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
  	
  	driver.navigate().refresh();
  	
  	String siteURL = driver.getCurrentUrl();
	System.out.println("site url="+siteURL);
    System.out.println("Page title is: " + driver.getTitle());
    
    List<HarEntry> en=proxy.getHar().getLog().getEntries();       
    System.out.println("Size----"+en.size());
 	
 	for(HarEntry hr:en){
 	
 	 if(hr.getRequest().getUrl().contains("google-analytics.com") && hr.getResponse().getContent().getMimeType().contains("image/gif"))
 	 {
 		 System.out.println("=========google-analytics.com=============");
         System.out.println("hr.getRequest().getUrl()="+hr.getRequest().getUrl());
         urlQueryString=hr.getRequest().getQueryString().toString();
         System.out.println("hr.getRequest().getQueryString()="+hr.getRequest().getQueryString());
         System.out.println("hr.getResponse().getStatus()="+hr.getResponse().getStatus());
         System.out.println("hr.getResponse().getStatusText()="+hr.getResponse().getStatusText());
         System.out.println("hr.getResponse().getContent().getMimeType()="+hr.getResponse().getContent().getMimeType());
         System.out.println("hr.getRequest().getMethod()="+hr.getRequest().getMethod());
         analyticsCheck = true;
 	 }
 	 else if(hr.getRequest().getUrl().contains("dc.optimahub.com"))
	 {
		 System.out.println("==========dc.optimahub.com=============");
         System.out.println("hr.getRequest().getUrl()="+hr.getRequest().getUrl());
         System.out.println("hr.getRequest().getQueryString()="+hr.getRequest().getQueryString());
         System.out.println("hr.getResponse().getStatus()="+hr.getResponse().getStatus());
         System.out.println("hr.getResponse().getStatusText()="+hr.getResponse().getStatusText());
         System.out.println("hr.getResponse().getContent().getMimeType()="+hr.getResponse().getContent().getMimeType());
         optimahubCheck = true;
	 }
 	 
 	}
 	
 	Assert.assertTrue(analyticsCheck, "There was NO network image request made to host www.google-analytics.com!");

 	Assert.assertTrue(optimahubCheck, "There was NO network request made to host: dc.optimahub.com!");
}

@Test(dependsOnMethods = "task2")
public void task3() throws Exception{

  System.out.println("In Task 3()");
  System.out.println("urlQueryString="+urlQueryString);
  
  String dtValue = StringUtils.substringBetween(urlQueryString, "dt=", ",");
  System.out.println("dtValue="+dtValue);
  
  String dpValue = StringUtils.substringBetween(urlQueryString, "dp=", ",");
  System.out.println("dpValue="+dpValue);
  
  String dlValue = StringUtils.substringBetween(urlQueryString, "dl=", ",");
  System.out.println("dlValue="+dlValue);
  
  logIntoCSV(dtValue, dpValue, dlValue);
  
}

public void logIntoCSV(String dt, String dp, String dl) throws Exception{
	  
	PrintWriter writer = new PrintWriter(new File(System.getProperty("user.dir")+"\\LogData\\Task3.csv"));
    StringBuilder sb = new StringBuilder();
    
    sb.append("dt").append(",").append("dp").append(",").append("dl").append('\n');
    sb.append(dt).append(",").append(dp).append(",").append(dl).append('\n');
    
    writer.write(sb.toString());
    writer.close();
	  
}
	


  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {
	 
	 
  }

  @BeforeTest
  public void beforeTest() {
	  
	    proxy = new BrowserMobProxyServer();
	    proxy.start(0);

	    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

	    DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setJavascriptEnabled(true);
	    capabilities.setCapability("takesScreenshot", true);  
	    capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
		                     "C:\\Program Files\\PhantomJs\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
		  String [] phantomJsArgs = {"--ignore-ssl-errors=true"};
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,phantomJsArgs);
	    capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

		driver = new PhantomJSDriver(capabilities);
		
	    proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

	    proxy.newHar("datalicious.com");

  }

  @AfterTest
  public void afterTest() {
	  
	  if (driver != null) {
			proxy.stop();
			driver.quit();
		}
  }

}
