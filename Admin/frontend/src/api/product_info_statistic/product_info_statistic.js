// import request from '@/utils/request';

import service from "@/utils/request";
const BASE_API = "/productInfoStatistics";

export default {
product_info_shelf_life_datetime_statistic_13b09a91_count(data){
  return service({
    url: BASE_API + "/product_info_shelf_life_datetime_statistic_13b09a91_count",
    method: "post",
    data:data
  });
},
product_info_shelf_life_datetime_statistic_fabe9b9f_count(data){
  return service({
    url: BASE_API + "/product_info_shelf_life_datetime_statistic_fabe9b9f_count",
    method: "post",
    data:data
  });
},
product_info_shelf_life_datetime_statistic_b986a1ea_count(data){
  return service({
    url: BASE_API + "/product_info_shelf_life_datetime_statistic_b986a1ea_count",
    method: "post",
    data:data
  });
},
product_info_product_status_enum_product_status_enum_id_1_status_statistic_b8964b61_count(data){
  return service({
    url: BASE_API + "/product_info_product_status_enum_product_status_enum_id_1_status_statistic_b8964b61_count",
    method: "post",
    data:data
  });
},
product_info_product_status_enum_product_status_enum_id_1_status_statistic_858961b6_count(data){
  return service({
    url: BASE_API + "/product_info_product_status_enum_product_status_enum_id_1_status_statistic_858961b6_count",
    method: "post",
    data:data
  });
},
product_info_product_status_enum_product_status_enum_id_1_status_statistic_68c7c75c_count(data){
  return service({
    url: BASE_API + "/product_info_product_status_enum_product_status_enum_id_1_status_statistic_68c7c75c_count",
    method: "post",
    data:data
  });
},
};
