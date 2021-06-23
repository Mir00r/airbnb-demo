package com.airbnb.rentalprocessor.domains.households.controllers.web

import com.airbnb.authenticator.common.controllers.BaseCrudWebController
import com.airbnb.common.utils.ExceptionUtil
import com.airbnb.rentalprocessor.domains.households.models.dtos.HouseholdDto
import com.airbnb.rentalprocessor.domains.households.models.mappers.HouseholdMapper
import com.airbnb.rentalprocessor.domains.households.services.HouseholdService
import com.airbnb.rentalprocessor.routing.Route
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
class HouseholdWebController @Autowired constructor(
    private val householdService: HouseholdService,
    private val householdMapper: HouseholdMapper
) : BaseCrudWebController<HouseholdDto> {

    @GetMapping(Route.V1.WEB_SEARCH_HOUSEHOLDS)
    override fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        model: Model
    ): String {
        val entities = this.householdService.search(query, page, size)
        model.addAttribute("households", entities.map { this.householdMapper.map(it) })
        return "households/fragments/all"
    }

    @GetMapping(Route.V1.WEB_FIND_HOUSEHOLD)
    override fun find(
        @PathVariable("id") id: Long,
        model: Model
    ): String {
        val entity = this.householdService.find(id).orElseThrow { ExceptionUtil.notFound("Household", id) }
        model.addAttribute("household", this.householdMapper.map(entity))
        return "households/fragments/details"
    }

    @GetMapping(Route.V1.WEB_CREATE_HOUSEHOLD_PAGE)
    override fun createPage(model: Model): String {
        return "households/fragments/create"
    }

    @PostMapping(Route.V1.WEB_CREATE_HOUSEHOLD)
    override fun create(
        @Valid @ModelAttribute dto: HouseholdDto,
        redirectAttributes: RedirectAttributes
    ): String {
        val entity = this.householdService.save(this.householdMapper.map(dto, null))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.WEB_FIND_HOUSEHOLD.replace("{id}", entity.id.toString())}"
    }

    @GetMapping(Route.V1.WEB_UPDATE_HOUSEHOLD_PAGE)
    override fun updatePage(@PathVariable("id") id: Long, model: Model): String {
        val entity = this.householdService.find(id).orElseThrow { ExceptionUtil.notFound("Household", id) }
        model.addAttribute("household", this.householdMapper.map(entity))
        return "households/fragments/create"
    }

    @PostMapping(Route.V1.WEB_UPDATE_HOUSEHOLD)
    override fun update(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute dto: HouseholdDto,
        redirectAttributes: RedirectAttributes
    ): String {
        var entity = this.householdService.find(id).orElseThrow { ExceptionUtil.notFound("Household", id) }
        entity = this.householdService.save(this.householdMapper.map(dto, entity))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.WEB_FIND_HOUSEHOLD.replace("{id}", entity.id.toString())}"
    }

    @PostMapping(Route.V1.WEB_DELETE_HOUSEHOLD)
    override fun delete(
        @PathVariable("id") id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        this.householdService.delete(id, true)
        redirectAttributes.addFlashAttribute("message", "Deleted!!")
        return "redirect:${Route.V1.WEB_SEARCH_HOUSEHOLDS}";
    }

}
