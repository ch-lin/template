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

package ch.template.app.service;

import ch.platform.common.testing.DatabaseUnitTest;
import ch.template.app.dao.ItemDao;
import ch.template.app.dao.ItemDaoImpl;
import ch.template.common.exception.ItemExistException;
import ch.template.common.exception.ItemNotFoundException;
import ch.template.common.reply.ItemInfoReply;
import ch.template.domain.model.Item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TemplateServiceTest extends DatabaseUnitTest {

    private final String name = "name0";

    private final long num = 1;

    private final ItemDao itemDao = new ItemDaoImpl();

    private final TemplateServiceImpl templateService = new TemplateServiceImpl();

    private Item createItem(String name, Long num) {
        Item item = new Item();
        item.setName(name);
        item.setNum(num);
        return item;
    }

    @BeforeEach
    protected void prepare() throws ItemExistException {
        templateService.itemDao = itemDao;
        Item item = createItem(name, num);
        templateService.addItem(item);
    }

    @Test
    public void testAddItem() throws ItemExistException {
        final String name = "name1";
        final long num = 2;
        Item item = createItem(name, num);
        templateService.addItem(item);
        List<ItemInfoReply> results = templateService.getAllItems();
        assertEquals(2, results.size());
        assertEquals(name, results.get(1).getName());
    }

    private void checkAddItemWithIncorrectParam(String name, Long num) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Item item = createItem(name, num);
            templateService.addItem(item);
        });
    }

    @Test
    public void testAddItemWithIncorrectParam() {
        checkAddItemWithIncorrectParam(null, num);
        checkAddItemWithIncorrectParam("", num);
        checkAddItemWithIncorrectParam(name + "_", null);
        checkAddItemWithIncorrectParam(name + "_", -1L);
    }

    @Test
    public void testAddDuplicateItem() {
        final String name2 = name;
        final long num2 = num;
        Assertions.assertThrows(ItemExistException.class, () -> {
            Item item = createItem(name2, num2);
            templateService.addItem(item);
        });
    }

    @Test
    public void testGetItem() throws ItemNotFoundException {
        Item result = templateService.getItem(name);
        assertNotNull(result);
        assertEquals(name, result.getName());
    }

    private void checkGetItemWithIncorrectParam(String name) {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                templateService.getItem(name)
        );
    }

    @Test
    public void testGetItemWithIncorrectParam() {
        checkGetItemWithIncorrectParam(null);
        checkGetItemWithIncorrectParam("");
    }

    @Test
    public void testGetItemNotFound() {
        final String name2 = name + "_";
        Assertions.assertThrows(ItemNotFoundException.class, () ->
                templateService.getItem(name2)
        );
    }

    @Test
    public void testCleanup() {
        List<ItemInfoReply> results = templateService.getAllItems();
        assertTrue(results.size() > 0);
        templateService.cleanup();
        results = templateService.getAllItems();
        assertEquals(0, results.size());
    }

}