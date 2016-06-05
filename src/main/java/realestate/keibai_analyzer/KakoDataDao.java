package realestate.keibai_analyzer;

import java.util.List;

import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

import realestate.keibai_analyzer.entity.KakoData;
import realestate.keibai_analyzer.setting.AppConfig;


@Dao(config = AppConfig.class)
public interface KakoDataDao {

    @Select
    KakoData selectById(Integer id);

    @Insert
    int insert(KakoData kakoData);
    
    @BatchInsert
    int[] insert(List<KakoData> kakoDatas);

    @Update
    int update(KakoData kakoData);

    @Delete
    int delete(KakoData kakoData);
    
    @Select
    List<KakoDataCityAverage> selectCityAverage();

}
