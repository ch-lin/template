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

import ch.template.app.service.TemplateService;
import ch.template.common.exception.ItemExistException;
import ch.template.common.exception.ItemNotFoundException;
import ch.template.domain.model.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RestController
public class Controller {

    @Autowired
    private TemplateService templateService;

    @PostMapping(value = "/item/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItem(@RequestBody final Item item, final @Context HttpServletResponse response) {
        try {
            templateService.addItem(item);
        } catch (IllegalArgumentException e) {
            response.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            return Response.status(Response.Status.BAD_REQUEST).entity("Parameters incorrect!").build();
        } catch (ItemExistException e) {
            response.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
            return Response.status(Response.Status.BAD_REQUEST).entity("Item already exist!").build();
        }
        return Response.status(Response.Status.OK).entity("OK").build();
    }

    @GetMapping(value = "/item/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Item getItem(final @QueryParam("name") String name, final @Context HttpServletResponse response) {
        try {
            return templateService.getItem(name);
        } catch (IllegalArgumentException e) {
            response.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
        } catch (ItemNotFoundException e) {
            response.setStatus(Response.Status.NOT_FOUND.getStatusCode());
        }
        return null;
    }

}