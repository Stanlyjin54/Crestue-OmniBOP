package com.kuafuweb.common.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;

import com.kuafuweb.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import static com.kuafuweb.common.util.ReflectUtils.createDateStyle;


@Component
@Slf4j
public class ExcelProvider {


    private static final int BATCH_COUNT = 100;


    /**
     * 导入 Excel 数据
     * @param file excel文件
     * @param clazz 实体类
     * @param saveFunction Servcie
     * @param <T> 实体类范型
     * @throws IOException
     */
    @Transactional
    public <T> void importData(MultipartFile file, Class<T> clazz, Consumer<List<T>> saveFunction)  {

        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, clazz, new ReadListener<T>() {
                private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

                @Override
                public void invoke(T data, AnalysisContext context) {
                    cachedDataList.add(data);
                    if (cachedDataList.size() >= BATCH_COUNT) {
                        saveData();
                        cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    saveData();
                }

                private void saveData() {
                    if (cachedDataList.isEmpty()) {
                        throw new BusinessException("导入的数据为空");
                    }
                    try {
                        saveFunction.accept(cachedDataList);
                    } catch (Exception e) {
                        throw new BusinessException("数据导入失败，列名或数据可能不匹配");
                    }
                }
            }).sheet().doRead();
        } catch (ExcelDataConvertException excelDataConvertException) {
            throw new BusinessException("请根据模版对比,检查excel中数据该是文本还是数字或者是表头与数据错误");
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

    }

    /**
     * 下载 Excel 模版
     * @return WorkBook
     */
    public Workbook downloadExcelTemplate(Class<?> clazz) {
        // 1. 创建Workbook并设置表头
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("模板");
        Row headerRow = sheet.createRow(0);
        // 设置表头背景色
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());

        // 2. 反射获取字段注解生成表头
        Field[] fields = clazz.getDeclaredFields();
        int cell = 0;
        for (Field field : fields) {
            ExcelProperty excelPropertyAnno = field.getAnnotation(ExcelProperty.class);
            if (excelPropertyAnno != null) {
                String[] value = excelPropertyAnno.value();
                if (value.length > 0) {
                    Cell headerRowCell = headerRow.createCell(cell);
                    headerRowCell.setCellValue(value[0]);
                    headerRowCell.setCellStyle(headerStyle);
                    cell++;
                }
            }
        }
        // 创建行数据
        Row dataRow = sheet.createRow(1);
        int cellIndex = 0;
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            Cell dataRowCell = dataRow.createCell(cellIndex);
            ExcelProperty excelPropertyAnno = field.getAnnotation(ExcelProperty.class);
            if (excelPropertyAnno != null) {
                // 类型判断逻辑
                if (fieldType == String.class) {
                    dataRowCell.setCellValue("这里写入文字");
                } else if (Integer.class.isAssignableFrom(fieldType)) {
                    dataRowCell.setCellValue((1));
                } else if (Double.class.isAssignableFrom(fieldType)) {
                    dataRowCell.setCellValue((1.0));
                } else if (Number.class.isAssignableFrom(fieldType)) {
                    dataRowCell.setCellValue(((Number) 1.0).doubleValue());
                } else if (fieldType == Date.class) {
                    dataRowCell.setCellValue(new Date());
                    CellStyle dateStyle = createDateStyle(dataRowCell.getSheet().getWorkbook());
                    dataRowCell.setCellStyle(dateStyle);
                } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                    dataRowCell.setCellValue(true);
                } else {
                    dataRowCell.setCellValue("其它");
                }
                cellIndex++;
            }
        }
        return workbook;
    }
}





