/*=============================================================================
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Che-Hung Lin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *===========================================================================*/

package ch.template.web.api;

import ch.platform.common.testing.DatabaseUnitTest;
import ch.template.domain.model.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest extends DatabaseUnitTest {

    @Autowired
    private MockMvc mvc;

    private final static String addItemURL = "/item/add";

    private final static String getItemURL = "/item/get";

    private final String name = "Item0";

    private final long num = 1;

    private final int OK = Response.Status.OK.getStatusCode();

    private final int NOT_FOUND = Response.Status.NOT_FOUND.getStatusCode();

    private final int BAD_REQUEST = Response.Status.BAD_REQUEST.getStatusCode();

    private Item createItem(String name, Long num) {
        Item item = new Item();
        item.setName(name);
        item.setNum(num);
        return item;
    }

    private static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    private MvcResult sendAddItem(Item item) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(addItemURL).contentType(MediaType.APPLICATION_JSON);
        if (item != null) {
            requestBuilder.content(asJsonString(item));
        }
        return mvc.perform(requestBuilder.accept(MediaType.APPLICATION_JSON)).andReturn();
    }

    private int sendAddItemAndGetStatus(Item item) throws Exception {
        MvcResult result = sendAddItem(item);
        return result.getResponse().getStatus();
    }

    @Test
    public void addItem() throws Exception {
        Item item = createItem(name, num);
        int status = sendAddItemAndGetStatus(item);
        assertEquals(OK, status);
    }

    @Test
    public void addDuplicateItem() throws Exception {
        Item item = createItem(name, num);
        sendAddItemAndGetStatus(item);
        int status = sendAddItemAndGetStatus(item);
        assertEquals(BAD_REQUEST, status);
    }

    private void checkAddIncorrectItem(Item item) throws Exception {
        int status = sendAddItemAndGetStatus(item);
        assertEquals(BAD_REQUEST, status);
    }

    @Test
    public void addIncorrectItem() throws Exception {
        checkAddIncorrectItem(null);
        Item item = createItem(null, num);
        checkAddIncorrectItem(item);
        item = createItem("", num);
        checkAddIncorrectItem(item);
        item = createItem(name, null);
        checkAddIncorrectItem(item);
        item = createItem(name, -1L);
        checkAddIncorrectItem(item);
    }

    private MvcResult sendGetItem(String name) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(getItemURL);
        if (name != null) {
            requestBuilder.param("name", name);
        }
        return mvc.perform(requestBuilder.accept(MediaType.APPLICATION_JSON)).andReturn();
    }

    @Test
    public void getItem() throws Exception {
        Item item = createItem(name, num);
        sendAddItem(item);
        MvcResult result = sendGetItem(name);
        int status = result.getResponse().getStatus();
        assertEquals(OK, status);
        Item itemResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Item.class);
        assertEquals(name, itemResult.getName());
        assertEquals(num, itemResult.getNum());
    }

    private int sendGetItemAndGetStatus(String name) throws Exception {
        MvcResult result = sendGetItem(name);
        return result.getResponse().getStatus();
    }

    @Test
    public void getNotExistItem() throws Exception {
        int status = sendGetItemAndGetStatus(name);
        assertEquals(NOT_FOUND, status);
    }

    @Test
    public void getItemWithIncorrectParam() throws Exception {
        int status = sendGetItemAndGetStatus("");
        assertEquals(BAD_REQUEST, status);
        status = sendGetItemAndGetStatus(null);
        assertEquals(BAD_REQUEST, status);
    }

}