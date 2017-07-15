package cn.sinjinsong.common.util;

import cn.sinjinsong.common.converter.POVOConverter;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SinjinSong on 2017/6/1.
 */
public class PageUtil {
    public static <T,R> PageInfo<R> convertPage(PageInfo<T> src, POVOConverter<T,R> converter) {
        List<R> list = new ArrayList<>();
        for(T t:src.getList()){
            list.add(converter.apply(t));
        }
        PageInfo<R> pageInfo = new PageInfo<>();
        pageInfo.setList(list);
        pageInfo.setHasNextPage(src.isHasNextPage());
        pageInfo.setHasPreviousPage(src.isHasPreviousPage());
        pageInfo.setIsFirstPage(src.isIsFirstPage());
        pageInfo.setNavigateFirstPage(src.getNavigateFirstPage());
        pageInfo.setIsLastPage(src.isIsLastPage());
        pageInfo.setNavigateLastPage(src.getNavigateLastPage());
        pageInfo.setNavigatepageNums(src.getNavigatepageNums());
        pageInfo.setNextPage(src.getNextPage());
        pageInfo.setPageNum(src.getPageNum());
        pageInfo.setPages(src.getPages());
        pageInfo.setPrePage(src.getPrePage());
        pageInfo.setPageSize(src.getPageSize());
        pageInfo.setSize(src.getSize());
        pageInfo.setStartRow(src.getStartRow());
        pageInfo.setEndRow(src.getEndRow());
        pageInfo.setTotal(src.getTotal());
        pageInfo.setNavigatePages(src.getNavigatePages());
        pageInfo.setLastPage(src.getLastPage());
        pageInfo.setFirstPage(src.getFirstPage());
        return pageInfo;
    }
    
    public static <E> PageInfo<E> convertToGeneralPage(Page<E> raw){
        PageInfo<E> pageInfo = new PageInfo<>();
        pageInfo.setList(raw.getContent());
        pageInfo.setTotal(raw.getTotalElements());
        pageInfo.setPages(raw.getTotalPages());
        pageInfo.setPageNum(raw.getNumber() + 1);
        pageInfo.setPageSize(raw.getSize());
        pageInfo.setSize(raw.getNumberOfElements());
        return pageInfo;
    }
}
