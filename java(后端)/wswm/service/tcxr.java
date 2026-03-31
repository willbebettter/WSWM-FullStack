package wswm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wswm.bean.mixgoodsbean;
import wswm.mapper.mixgoods;

import java.util.List;

@Slf4j
@Service
public class tcxr {
    @Autowired
    private mixgoods mix;

    public List<mixgoodsbean> allgoods() {
        try {
            List<mixgoodsbean> gd = mix.getmixgoods();
            return gd;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean add(mixgoodsbean gd, String listStr) {
        try {
            mix.addmixgoods(gd);
            Integer id = mix.getmixid(gd.getMix_name());
            // 将逗号分隔的字符串转换为菜品名称列表
            List<String> dishNames = java.util.Arrays.asList(listStr.split(","));
            mix.addco1(id, dishNames);
            return true;
        } catch (Exception e) {
            log.warn("添加失败" + e);
            return false;
        }
    }

    public String fin(Integer id) {
        try {
            return mix.find(id);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean del(Integer id) {
        try {
            mix.del(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public mixgoodsbean getgood(Integer id) {
        try {
            mixgoodsbean gd = mix.getgood(id);
            return gd;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean update(mixgoodsbean gd) {
        try {
            mix.update(gd);
            return true;
        } catch (Exception e) {
            log.warn("更新失败{}", String.valueOf(e));
            return false;
        }
    }
    public boolean update1(mixgoodsbean gd) {
        try {
            mix.update(gd);
            return true;
        } catch (Exception e) {
            log.warn("更新失败{}", String.valueOf(e));
            return false;
        }
    }

    public List<mixgoodsbean> type(String type_name) {
        try {
            List<mixgoodsbean> gd = mix.type(type_name);
            return gd;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean delco(Integer id) {
        try {
            mix.delco(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Integer> getmixgoodids(Integer id) {
        try {
            List<Integer> gd = mix.getco2(id);
            return gd;
        } catch (Exception e) {
            return null;
        }
    }

    public String getmixgoodnames(Integer id) {
        try {
            String gd = mix.getco3(id);
            return gd;
        } catch (Exception e) {
            log.warn("获取失败{}", String.valueOf(e));
            return null;
        }
    }

    public boolean updateco(Integer id, String list) {
        try {
            List<String> dishNames = java.util.Arrays.asList(list.split(","));
            mix.delco(id);
            mix.addco1(id, dishNames);
            return true;
        } catch (Exception e) {
            log.warn("更新失败{}", String.valueOf(e));
            return false;
        }
    }

    public boolean dsemix1(Integer id, String mix_name) {
        try {
            Integer x = mix.dsemix1(id, mix_name);
            if (x == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.warn("查找失败{}", String.valueOf(e));
            return false;
        }
    }

    public boolean dsemix2(String mixName) {
        try {
            Integer x = mix.dsemix2(mixName);
            if (x == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.warn("查找失败{}", String.valueOf(e));
            return false;
        }
    }
}
