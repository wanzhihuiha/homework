package com.example.demo.Mapper;


import com.example.demo.Model.test_report;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface test_report_mapper {
    List<test_report> gettest_report();
    int new_insert_report(test_report test_report);
    int updata_report(test_report test_report);
    test_report get_id(int id);

}
