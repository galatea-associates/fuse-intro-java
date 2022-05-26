package org.galatea.starter.entrypoint;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import junitparams.JUnitParamsRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.ASpringTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@RequiredArgsConstructor
@Slf4j
// We need to do a full application start up for this one, since we want the feign clients to be instantiated.
// It's possible we could do a narrower slice of beans, but it wouldn't save that much test run time.
@SpringBootTest
// this gives us the MockMvc variable
@AutoConfigureMockMvc
// we previously used WireMockClassRule for consistency with ASpringTest, but when moving to a dynamic port
// to prevent test failures in concurrent builds, the wiremock server was created too late and feign was
// already expecting it to be running somewhere else, resulting in a connection refused
@AutoConfigureWireMock(port = 0, files = "classpath:/wiremock")
// Use this runner since we want to parameterize certain tests.
// See runner's javadoc for more usage.
@RunWith(JUnitParamsRunner.class)
public class IexRestControllerTest extends ASpringTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void testGetSymbolsEndpoint() throws Exception {
    MvcResult result = this.mvc.perform(
        // note that we were are testing the fuse REST end point here, not the IEX end point.
        // the fuse end point in turn calls the IEX end point, which is WireMocked for this test.
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/iex/symbols")
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        // some simple validations, in practice I would expect these to be much more comprehensive.
        .andExpect(jsonPath("$[0].symbol", is("A")))
        .andExpect(jsonPath("$[1].symbol", is("AA")))
        .andExpect(jsonPath("$[2].symbol", is("AAAU")))
        .andReturn();
  }

  @Test
  public void testGetLastTradedPrice() throws Exception {

    MvcResult result = this.mvc.perform(
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .get("/iex/lastTradedPrice?symbols=FB")
            // This URL will be hit by the MockMvc client. The result is configured in the file
            // src/test/resources/wiremock/mappings/mapping-lastTradedPrice.json
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].symbol", is("FB")))
        .andExpect(jsonPath("$[0].price").value(new BigDecimal("186.34")))
        .andReturn();
  }

  @Test
  public void testGetLastTradedPriceEmpty() throws Exception {

    MvcResult result = this.mvc.perform(
        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
            .get("/iex/lastTradedPrice?symbols=")
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", is(Collections.emptyList())))
        .andReturn();
  }

  @Test
  public void testGetHistoricalPrice() throws Exception {

    MvcResult result = this.mvc.perform(
        MockMvcRequestBuilders.get(
                "/iex/historicalPrice?symbol=fb")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].close", is(180.95)))
            .andExpect(jsonPath("$[0].high", is(187.185)))
            .andExpect(jsonPath("$[0].low", is(179.5)))
            .andExpect(jsonPath("$[0].open", is(186.63)))
            .andExpect(jsonPath("$[0].symbol", is("FB")))
            .andExpect(jsonPath("$[0].volume", is(31747434)))
            .andExpect(jsonPath("$[0].date", is("2022-04-26")))
            .andReturn();
  }

  @Test
  public void testGetHistoricalPriceRange() throws Exception {

    MvcResult result = this.mvc.perform(
            MockMvcRequestBuilders.get(
                    "/iex/historicalPrice?symbol=aapl&range=1m")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].close", is(156.8)))
            .andExpect(jsonPath("$[0].high", is(162.34)))
            .andExpect(jsonPath("$[0].low", is(156.72)))
            .andExpect(jsonPath("$[0].open", is(162.25)))
            .andExpect(jsonPath("$[0].symbol", is("AAPL")))
            .andExpect(jsonPath("$[0].volume", is(95623240)))
            .andExpect(jsonPath("$[0].date", is("2022-04-26")))
            .andReturn();
  }

  @Test
  public void testGetHistoricalPriceDate() throws Exception {

    MvcResult result = this.mvc.perform(
            MockMvcRequestBuilders.get(
                    "/iex/historicalPrice?symbol=aapl&date=20220505")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].close", is(163.805)))
            .andExpect(jsonPath("$[0].high", is(163.95)))
            .andExpect(jsonPath("$[0].low", is(163.54)))
            .andExpect(jsonPath("$[0].open", is(163.8)))
            .andExpect(jsonPath("$[0].volume", is(27431)))
            .andExpect(jsonPath("$[0].date", is("2022-05-05")))
            .andReturn();
  }

  @Test
  public void testGetHistoricalPriceNoSymbol() throws Exception {

    MvcResult result = this.mvc.perform(
            MockMvcRequestBuilders.get(
                    "/iex/historicalPrice")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();
  }
}
