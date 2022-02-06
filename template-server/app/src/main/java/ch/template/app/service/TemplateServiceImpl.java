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

import ch.template.app.dao.ItemDao;
import ch.template.common.exception.ItemExistException;
import ch.template.common.exception.ItemNotFoundException;
import ch.template.common.reply.ItemInfoReply;
import ch.template.domain.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    public ItemDao itemDao;

    public void addItem(Item item) throws IllegalArgumentException, ItemExistException {
        if (item.getNum() == null || item.getNum() < 0 ||
                item.getName() == null || item.getName().isBlank()) {
            throw new IllegalArgumentException();
        }
        itemDao.addItem(item);
    }

    public Item getItem(String name) throws IllegalArgumentException, ItemNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException();
        }
        return itemDao.getItem(name);
    }

    public List<ItemInfoReply> getAllItems() {
        List<ItemInfoReply> result = new ArrayList<>();
        for (Item e : itemDao.getAllItems()) {
            ItemInfoReply response = new ItemInfoReply();
            response.setName(e.getName());
            response.setNum(e.getNum());
            result.add(response);
        }
        return result;
    }

    public int cleanup() {
        return itemDao.cleanup();
    }

}