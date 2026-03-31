package wswm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wswm.bean.goodsbean;
import wswm.mapper.goods;

import java.util.List;

@Slf4j
@Service
public class spxr {
    @Autowired
    private goods goods;

    public List<goodsbean> allgoods() {
        try {
            List<goodsbean> gd = goods.allgoods();
            return gd;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean addgood(goodsbean gd) {
        try {
            goods.addgoods(gd);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delgood(Integer id) {
        try {
            goods.delgoods(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String origood(Integer id) {
        try {
            String gdfile = goods.getgoodphoto(id);
            return gdfile;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updgood(goodsbean gd) {
        try {
            goods.updgoods(gd);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public goodsbean getgood(Integer id) {
        try {
            goodsbean gd = goods.getgood(id);
            return gd;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updagood(goodsbean gd) {
        try {
            goods.updgoods2(gd);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<goodsbean> gettype(String type_name) {
        try {
            List<goodsbean> goo = goods.getgoodsbytype(type_name);
            return goo;
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getgoodsname() {
        try {
            List<String> li = goods.getgoodsname();
            return li;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean glcz(Integer id) {
        try {
            List<Integer> xx = goods.glgood(id);
            if (xx.isEmpty()) {
                return true;
            } else return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean select1(Integer id, String good_name) {
        try {
            Integer xx = goods.getgoodname(id, good_name);
            if (xx == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean select2(String goodName) {
        try {
            Integer x = goods.getgoodname2(goodName);
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
