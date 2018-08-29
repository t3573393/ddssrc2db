package org.fartpig.ddssrc2db.web;

import java.util.ArrayList;
import java.util.List;

import org.fartpig.ddssrc2db.consts.ResolverConst;
import org.fartpig.ddssrc2db.dto.ColumnDTO;
import org.fartpig.ddssrc2db.dto.SearchDTO;
import org.fartpig.ddssrc2db.dto.SearchResultDTO;
import org.fartpig.ddssrc2db.dto.TableDetailDTO;
import org.fartpig.ddssrc2db.dto.TableIndexDTO;
import org.fartpig.ddssrc2db.dto.ViewConditionDTO;
import org.fartpig.ddssrc2db.model.Columns;
import org.fartpig.ddssrc2db.model.TableIndex;
import org.fartpig.ddssrc2db.model.Tables;
import org.fartpig.ddssrc2db.model.ViewCondition;
import org.fartpig.ddssrc2db.service.ColumnsService;
import org.fartpig.ddssrc2db.service.TableIndexService;
import org.fartpig.ddssrc2db.service.TablesService;
import org.fartpig.ddssrc2db.service.ViewConditionService;
import org.fartpig.ddssrc2db.util.ResolverUtils;
import org.fartpig.ddssrc2db.util.StringUtils;
import org.fartpig.ddssrc2db.util.ViewUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

	@Autowired
	TablesService tableService;

	@Autowired
	ColumnsService columnService;

	@Autowired
	TableIndexService tableIndexService;

	@Autowired
	ViewConditionService viewConditionService;

	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Model model) {
		return "index";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		return "home";
	}

	@RequestMapping(value = "/list_tables", method = RequestMethod.GET)
	public String listTables(Model model) {
		Sort tableOrder = ResolverUtils.createTablesSort();
		Pageable pageRequest = ResolverUtils.createPageable(1, ResolverConst.PAGE_SIZE, tableOrder);
		Page<Tables> pageResult = tableService.findAll(pageRequest);
		model.addAttribute("result", pageResult);

		List<SearchResultDTO> resultDto = new ArrayList<SearchResultDTO>();
		for (Tables aTables : pageResult.getContent()) {
			SearchResultDTO dto = new SearchResultDTO();
			BeanUtils.copyProperties(aTables, dto);
			dto.setTableComment(ViewUtils.renderComment(dto.getTableComment()));

			resultDto.add(dto);
		}
		model.addAttribute("resultDto", resultDto);
		return "list_tables";
	}

	@RequestMapping(value = "/search_tables", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchTables(@ModelAttribute("SearchDTO") SearchDTO searchDto, Model model) {
		Sort tableOrder = ResolverUtils.createTablesSort();
		Pageable pageRequest = ResolverUtils.createPageable(searchDto.getPageIndex(), ResolverConst.PAGE_SIZE,
				tableOrder);
		Page<Tables> pageResult = tableService.findByTableNameAndTableType(searchDto.getTableName(),
				searchDto.getTableType(), pageRequest);
		model.addAttribute("result", pageResult);

		List<SearchResultDTO> resultDto = new ArrayList<SearchResultDTO>();
		for (Tables aTables : pageResult.getContent()) {
			SearchResultDTO dto = new SearchResultDTO();
			BeanUtils.copyProperties(aTables, dto);
			dto.setTableComment(ViewUtils.renderComment(dto.getTableComment()));

			resultDto.add(dto);
		}
		model.addAttribute("resultDto", resultDto);

		model.addAttribute("searchDto", searchDto);

		return "list_tables";
	}

	@RequestMapping(value = "/list_columns", method = RequestMethod.GET)
	public String listColumns(Model model) {
		Sort columnOrder = ResolverUtils.createTableColumnsSort();
		Pageable pageRequest = ResolverUtils.createPageable(1, ResolverConst.PAGE_SIZE, columnOrder);

		Page<Columns> pageResult = columnService.findAll(pageRequest);
		model.addAttribute("result", pageResult);

		List<SearchResultDTO> resultDto = new ArrayList<SearchResultDTO>();
		for (Columns aColumns : pageResult.getContent()) {
			SearchResultDTO dto = new SearchResultDTO();
			BeanUtils.copyProperties(aColumns, dto);
			dto.setColumnComment(ViewUtils.renderComment(dto.getColumnComment()));
			dto.setDataType(ViewUtils.renderDataType(dto));

			Tables aTables = tableService.findFirstByTableNameAndTableType(dto.getTableName(), dto.getTableType());
			if (aTables != null) {
				dto.setTableId(aTables.getId());
			}

			resultDto.add(dto);
		}
		model.addAttribute("resultDto", resultDto);

		return "list_columns";
	}

	@RequestMapping(value = "/search_columns", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchColumns(@ModelAttribute("SearchDTO") SearchDTO searchDto, Model model) {
		Sort columnOrder = ResolverUtils.createTableColumnsSort();
		Pageable pageRequest = ResolverUtils.createPageable(searchDto.getPageIndex(), ResolverConst.PAGE_SIZE,
				columnOrder);
		Page<Columns> pageResult = null;
		if (StringUtils.isNullOrEmpty(searchDto.getTableName())) {
			pageResult = columnService.findByColumnName(searchDto.getColumnName(), pageRequest);

		} else {
			pageResult = columnService.findByTableNameAndTableTypeAndColumnName(searchDto.getTableName(),
					searchDto.getTableType(), searchDto.getColumnName(), pageRequest);
		}
		model.addAttribute("result", pageResult);

		List<SearchResultDTO> resultDto = new ArrayList<SearchResultDTO>();
		for (Columns aColumns : pageResult.getContent()) {
			SearchResultDTO dto = new SearchResultDTO();
			BeanUtils.copyProperties(aColumns, dto);
			dto.setColumnComment(ViewUtils.renderComment(dto.getColumnComment()));
			dto.setDataType(ViewUtils.renderDataType(dto));

			Tables aTables = tableService.findFirstByTableNameAndTableType(dto.getTableName(), dto.getTableType());
			if (aTables != null) {
				dto.setTableId(aTables.getId());
			}

			resultDto.add(dto);
		}
		model.addAttribute("resultDto", resultDto);
		model.addAttribute("searchDto", searchDto);

		return "list_columns";
	}

	@RequestMapping(value = "/load_table_detail", method = RequestMethod.GET)
	public String loadTableDetail(@RequestParam(value = "id", required = true) String id, Model model) {

		Tables aTable = tableService.findOne(Long.valueOf(id));
		List<TableIndex> tableIndexs = tableIndexService.findByTableNameAndTableType(aTable.getTableName(),
				aTable.getTableType());
		List<ViewCondition> viewConditions = viewConditionService.findByTableNameAndTableType(aTable.getTableName(),
				aTable.getTableType());
		List<Columns> columns = columnService.findAllByTableNameAndTableType(aTable.getTableName(),
				aTable.getTableType());

		TableDetailDTO tableDetailDTO = new TableDetailDTO();
		BeanUtils.copyProperties(aTable, tableDetailDTO);
		tableDetailDTO.setTableComment(ViewUtils.renderComment(tableDetailDTO.getTableComment()));
		model.addAttribute("tableDetailDTO", tableDetailDTO);

		String refTables = aTable.getRefTables();
		Tables refDBTables = tableService.findFirstByTableNameAndTableType(refTables, ResolverConst.PF_FILE_TYPE);
		if (refDBTables != null) {
			model.addAttribute("refTablesId", refDBTables.getId());
		}

		List<ColumnDTO> columnDTOs = new ArrayList<ColumnDTO>();
		for (Columns aColumns : columns) {
			ColumnDTO dto = new ColumnDTO();
			BeanUtils.copyProperties(aColumns, dto);
			dto.setColumnComment(ViewUtils.renderComment(dto.getColumnComment()));
			dto.setDataType(ViewUtils.renderDataType(dto));
			columnDTOs.add(dto);
		}
		model.addAttribute("columns", columnDTOs);

		List<TableIndexDTO> tableIndexDTOs = new ArrayList<TableIndexDTO>();
		for (TableIndex aTableIndex : tableIndexs) {
			TableIndexDTO dto = new TableIndexDTO();
			BeanUtils.copyProperties(aTableIndex, dto);
			dto.setIndexComment(ViewUtils.renderComment(dto.getIndexComment()));
			tableIndexDTOs.add(dto);
		}
		model.addAttribute("tableIndexs", tableIndexDTOs);

		List<ViewConditionDTO> viewConditionDTOs = new ArrayList<ViewConditionDTO>();
		for (ViewCondition aViewCondition : viewConditions) {
			ViewConditionDTO dto = new ViewConditionDTO();
			BeanUtils.copyProperties(aViewCondition, dto);
			dto.setConditionComment(ViewUtils.renderComment(dto.getConditionComment()));
			viewConditionDTOs.add(dto);
		}
		model.addAttribute("viewConditions", viewConditionDTOs);

		return "table_detail";
	}

}
