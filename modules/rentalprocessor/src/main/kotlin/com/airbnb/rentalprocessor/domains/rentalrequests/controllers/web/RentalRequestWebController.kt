package com.airbnb.rentalprocessor.domains.rentalrequests.controllers.web

import com.airbnb.authenticator.common.controllers.BaseCrudWebController
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.rentalprocessor.domains.rentalrequests.models.dtos.RentalRequestDto
import com.airbnb.rentalprocessor.domains.rentalrequests.models.mappers.RentalRequestMapper
import com.airbnb.rentalprocessor.domains.rentalrequests.services.RentalRequestService
import com.airbnb.rentalprocessor.routing.Route
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
class RentalRequestWebController @Autowired constructor(
    private val rentalRequestService: RentalRequestService,
    private val rentalRequestMapper: RentalRequestMapper
) : BaseCrudWebController<RentalRequestDto> {

    @GetMapping(Route.V1.WEB_SEARCH_RENTALREQUESTS)
    override fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        model: Model
    ): String {
        val entities = this.rentalRequestService.search(query, page, size)
        model.addAttribute("rentalrequests", entities.map { this.rentalRequestMapper.map(it) })
        return "rentalrequests/fragments/all"
    }

    @GetMapping(Route.V1.WEB_FIND_RENTALREQUEST)
    override fun find(
        @PathVariable("id") id: Long,
        model: Model
    ): String {
        val entity = this.rentalRequestService.find(id).orElseThrow { ExceptionUtil.notFound("RentalRequest", id) }
        model.addAttribute("rentalrequest", this.rentalRequestMapper.map(entity))
        return "rentalrequests/fragments/details"
    }

    @GetMapping(Route.V1.WEB_CREATE_RENTALREQUEST_PAGE)
    override fun createPage(model: Model): String {
        return "rentalrequests/fragments/create"
    }

    @PostMapping(Route.V1.WEB_CREATE_RENTALREQUEST)
    override fun create(
        @Valid @ModelAttribute dto: RentalRequestDto,
        redirectAttributes: RedirectAttributes
    ): String {
        val entity = this.rentalRequestService.save(this.rentalRequestMapper.map(dto, null))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.WEB_FIND_RENTALREQUEST.replace("{id}", entity.id.toString())}"
    }

    @GetMapping(Route.V1.WEB_UPDATE_RENTALREQUEST_PAGE)
    override fun updatePage(@PathVariable("id") id: Long, model: Model): String {
        val entity = this.rentalRequestService.find(id).orElseThrow { ExceptionUtil.notFound("RentalRequest", id) }
        model.addAttribute("rentalrequest", this.rentalRequestMapper.map(entity))
        return "rentalrequests/fragments/create"
    }

    @PostMapping(Route.V1.WEB_UPDATE_RENTALREQUEST)
    override fun update(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute dto: RentalRequestDto,
        redirectAttributes: RedirectAttributes
    ): String {
        var entity = this.rentalRequestService.find(id).orElseThrow { ExceptionUtil.notFound("RentalRequest", id) }
        entity = this.rentalRequestService.save(this.rentalRequestMapper.map(dto, entity))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.WEB_FIND_RENTALREQUEST.replace("{id}", entity.id.toString())}"
    }

    @PostMapping(Route.V1.WEB_DELETE_RENTALREQUEST)
    override fun delete(
        @PathVariable("id") id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        this.rentalRequestService.delete(id, true)
        redirectAttributes.addFlashAttribute("message", "Deleted!!")
        return "redirect:${Route.V1.WEB_SEARCH_RENTALREQUESTS}";
    }

}
