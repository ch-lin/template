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

package ch.template.domain.model;

import ch.platform.common.testing.DatabaseUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemTest extends DatabaseUnitTest {

    private final String name = "Item1";

    private final Long num = 1L;

    @BeforeEach
    public void prepare() {
        Item item = new Item();
        item.setName(name);
        item.setNum(num);
        em.persist(item);
    }

    @Test
    public void testSave() {
        final String newItemName = "Item2";
        Item item = new Item(newItemName, 1L);
        em.persist(item);
        TypedQuery<Item> query = em.createQuery("SELECT e FROM Item e WHERE e.name = :name", Item.class);
        List<Item> result = query.setParameter("name", newItemName).getResultList();
        assertEquals(1, result.size());
        assertEquals(newItemName, result.get(0).getName());
    }

    @Test
    public void testLoad() {
        TypedQuery<Item> query = em.createQuery("SELECT e FROM Item e", Item.class);
        List<Item> result = query.getResultList();
        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
        assertEquals(num, result.get(0).getNum());
        assertTrue(result.get(0).getItemID() > 0);
    }

    @Test
    public void testSaveNotNullOnName() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            Item item = new Item(null, 1L);
            em.persist(item);
            em.flush();
        });
    }

}