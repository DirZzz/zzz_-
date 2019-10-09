package com.sandu.search.entity.elasticsearch.dco;

import lombok.Data;
import org.apache.lucene.search.BooleanClause;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sandu
 * @ClassName MultiWildcardQuery
 * @date 2019/9/4-19:20$
 */
@Data
public class MultiWildcardQuery implements Serializable {
	//搜索关键字
	private String searchKeyword;
	//匹配字段列表
	private List<String> fieldList;

	private QueryBuilder query;

	public MultiWildcardQuery(String searchKeyword, List<String> matchFieldList, BooleanClause.Occur occur) {
		this.searchKeyword = searchKeyword;
		this.fieldList = matchFieldList;

		List<WildcardQueryBuilder> wildcardQueryBuilders = matchFieldList.stream()
				.map(it -> QueryBuilders.wildcardQuery(it, searchKeyword).boost(matchFieldList.size() - matchFieldList.indexOf(it)))
				.collect(Collectors.toList());
		BoolQueryBuilder finalQuery = QueryBuilders.boolQuery();
		switch (occur){
			case MUST:
				wildcardQueryBuilders.forEach(finalQuery::must);
				break;
			case SHOULD:
				wildcardQueryBuilders.forEach(finalQuery::should);
				break;
			default:
				break;
		}
		this.query = finalQuery;
	}


}
