package com.caubogeo.bogeo.publicdata;

import com.caubogeo.bogeo.domain.medicine.AvoidCombination;
import com.caubogeo.bogeo.domain.medicine.MedicineDetail;
import com.caubogeo.bogeo.domain.medicine.PillShape;
import com.caubogeo.bogeo.repository.CombinationRepository;
import com.caubogeo.bogeo.repository.MedicineDetailRepository;
import com.caubogeo.bogeo.repository.PillShapeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataApiService {
    private final PillShapeRepository pillShapeRepository;
    private final CombinationRepository combinationRepository;
    private final MedicineDetailRepository medicineDetailRepository;

    @Transactional
    public void setPillShapeDatabase() {
        try {
            for (int j = 1; j <= 248; j++) {
                URL url = new URL(
                        "http://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01?serviceKey=ZaEuGtM8LYExIc%2FxBYwBYjrB%2BB4Lmetl1CRgp%2FPrJGfJRYGQec%2Fr2mqMRAaDuoRUuolev3%2BO%2FmLtvl34LS%2Be2A%3D%3D&pageNo="
                                + j + "&numOfRows=100&type=json");
                log.debug("url = {}", url);
                BufferedReader bf;
                bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                String result = bf.readLine();
                JSONParser jsonParser = new JSONParser();
                JSONObject object = (JSONObject) jsonParser.parse(result);
                JSONObject body = (JSONObject) object.get("body");
                JSONArray items = (JSONArray) body.get("items");
                for (int i = 0; i < items.size(); i++) {
                    log.debug("i번째 값 : {} j번째 페이지 : {}", i, j);
                    JSONObject pill = (JSONObject) items.get(i);
                    String item_seq = (String) pill.get("ITEM_SEQ");
                    String item_name = (String) pill.get("ITEM_NAME");
                    String entp_name = (String) pill.get("ENTP_NAME");
                    String chart = (String) pill.get("CHART");
                    String image = (String) pill.get("ITEM_IMAGE");
                    List<String> mark_front;
                    List<String> mark_rear;
                    List<String> color_front;
                    List<String> color_rear;
                    if (pill.get("MARK_CODE_FRONT_ANAL") == null || ((String) pill.get(
                            "MARK_CODE_FRONT_ANAL")).isEmpty()) {
                        mark_front = null;
                    } else {
                        mark_front = Arrays.asList(((String) pill.get("MARK_CODE_FRONT_ANAL")).split("\\|"));
                    }
                    if (pill.get("MARK_CODE_BACK_ANAL") == null || ((String) pill.get(
                            "MARK_CODE_BACK_ANAL")).isEmpty()) {
                        mark_rear = null;
                    } else {
                        mark_rear = Arrays.asList(((String) pill.get("MARK_CODE_BACK_ANAL")).split("\\|"));
                    }
                    if (pill.get("COLOR_CLASS1") != null) {
                        color_front = Arrays.asList(((String) pill.get("COLOR_CLASS1")).split("\\|"));
                    } else {
                        color_front = null;
                    }
                    if (pill.get("COLOR_CLASS2") != null) {
                        color_rear = Arrays.asList(((String) pill.get("COLOR_CLASS2")).split("\\|"));
                    } else {
                        color_rear = null;
                    }
                    String shape = (String) pill.get("DRUG_SHAPE");
                    String class_name = (String) pill.get("CLASS_NAME");
                    String medicine_type = (String) pill.get("ETC_OTC_NAME");
                    String print_front = (String) pill.get("PRINT_FRONT");
                    String print_back = (String) pill.get("PRINT_BACK");
                    PillShape new_pill = PillShape.builder()
                            .itemSeq(item_seq)
                            .itemName(item_name)
                            .entpName(entp_name)
                            .chart(chart)
                            .image(image)
                            .shape(shape)
                            .colorFront(color_front)
                            .colorRear(color_rear)
                            .className(class_name)
                            .medicineType(medicine_type)
                            .printFront(print_front)
                            .printBack(print_back)
                            .markFront(mark_front)
                            .markRear(mark_rear)
                            .build();
                    pillShapeRepository.save(new_pill);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void setCombinationDatabase() {
        try {
            for (int i = 2884; i <= 4666; i++) {
                URL url = new URL(
                        "http://apis.data.go.kr/1471000/DURPrdlstInfoService01/getUsjntTabooInfoList?serviceKey=ZaEuGtM8LYExIc%2FxBYwBYjrB%2BB4Lmetl1CRgp%2FPrJGfJRYGQec%2Fr2mqMRAaDuoRUuolev3%2BO%2FmLtvl34LS%2Be2A%3D%3D&typeName=%EB%B3%91%EC%9A%A9%EA%B8%88%EA%B8%B0&pageNo="
                                + i + "&numOfRows=100&type=json");
                log.debug("url = {}", url);
                log.debug("i번째 페이지 : {}", i);
                BufferedReader bf;
                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                String result = bf.readLine();
                JSONParser jsonParser = new JSONParser();
                JSONObject object = (JSONObject) jsonParser.parse(result);
                JSONObject body = (JSONObject) object.get("body");
                JSONArray items = (JSONArray) body.get("items");
                for (int j = 0; j < items.size(); j++) {
                    JSONObject combination = (JSONObject) items.get(j);
                    String first_medicine_seq = (String) combination.get("ITEM_SEQ");
                    String second_medicine_seq = (String) combination.get("MIXTURE_ITEM_SEQ");
                    String first_medicine_name = (String) combination.get("ITEM_NAME");
                    String second_medicine_name = (String) combination.get("MIXTURE_ITEM_NAME");
                    String prohibited_content = (String) combination.get("PROHBT_CONTENT");
                    AvoidCombination avoidCombination = AvoidCombination.builder()
                            .firstMedicineSeq(first_medicine_seq)
                            .firstMedicineName(first_medicine_name)
                            .secondMedicineSeq(second_medicine_seq)
                            .secondMedicineName(second_medicine_name)
                            .prohibitedContent(prohibited_content)
                            .build();
                    if (i == 2884) {
                        if (!combinationRepository.existsByFirstMedicineSeqAndSecondMedicineSeq(
                                avoidCombination.getFirstMedicineSeq(), avoidCombination.getSecondMedicineSeq())) {
                            combinationRepository.save(avoidCombination);
                        }
                    } else {
                        combinationRepository.save(avoidCombination);
                    }
                }
                bf.close();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void setMedicineDetailDatabase() {
        try {
            for (int i = 1; i <= 516; i++) {
                URL url = new URL(
                        "http://apis.data.go.kr/1471000/DrugPrdtPrmsnInfoService02/getDrugPrdtPrmsnDtlInq01?serviceKey=ZaEuGtM8LYExIc%2FxBYwBYjrB%2BB4Lmetl1CRgp%2FPrJGfJRYGQec%2Fr2mqMRAaDuoRUuolev3%2BO%2FmLtvl34LS%2Be2A%3D%3D&pageNo="
                                + i + "&numOfRows=100&type=json");
                log.debug("url = {}", url);
                BufferedReader bf;
                bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                String result = bf.readLine();
                JSONParser jsonParser = new JSONParser();
                JSONObject object = (JSONObject) jsonParser.parse(result);
                JSONObject body = (JSONObject) object.get("body");
                JSONArray items = (JSONArray) body.get("items");
                for (int j = 0; j < items.size(); j++) {
                    JSONObject medicineDetailJson = (JSONObject) items.get(j);
                    String itemSeq = (String) medicineDetailJson.get("ITEM_SEQ");

                    PillShape pillShape = pillShapeRepository.findByItemSeq(itemSeq);
                    if (pillShape == null) {
                        continue;
                    }
                    String image = pillShape.getImage();
                    String className = pillShape.getClassName();
                    String itemName = (String) medicineDetailJson.get("ITEM_NAME");
                    String entpName = (String) medicineDetailJson.get("ENTP_NAME");
                    String medicineCode = (String) medicineDetailJson.get("ETC_OTC_CODE");
                    String storageMethod = (String) medicineDetailJson.get("STORAGE_METHOD");

                    String mainItemIngredient = (String) medicineDetailJson.get("MAIN_ITEM_INGR");
                    List<String> mainItemIngredientList = List.of(mainItemIngredient.split("\\|"));

                    String validTerm = (String) medicineDetailJson.get("VALID_TERM");

                    String medicineEffectRaw = (String) medicineDetailJson.get("EE_DOC_DATA");
                    String medicineDosageRaw = (String) medicineDetailJson.get("UD_DOC_DATA");
                    String medicineWarningRaw = (String) medicineDetailJson.get("NB_DOC_DATA");
                    String medicineEffect = parseDetailInformation(medicineEffectRaw);
                    String medicineDosage = parseDetailInformation(medicineDosageRaw);
                    String medicineWarning = parseDetailInformation(medicineWarningRaw);

                    MedicineDetail medicineDetail = MedicineDetail.builder()
                            .itemSeq(itemSeq)
                            .itemName(itemName)
                            .entpName(entpName)
                            .medicineCode(medicineCode)
                            .medicineEffect(medicineEffect)
                            .medicineDosage(medicineDosage)
                            .medicineWarning(medicineWarning)
                            .storageMethod(storageMethod)
                            .mainItemIngredient(mainItemIngredientList)
                            .validTerm(validTerm)
                            .image(image)
                            .className(className)
                            .build();
                    medicineDetailRepository.save(medicineDetail);
                }
                bf.close();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private String parseDetailInformation(String medicineDetailRaw) {
        String parsedString = medicineDetailRaw;
        String[] removeWords = {"\\r", "\\n", "&nbsp;", "SECTION title=\"", "ARTICLE title=\"", "PARAGRAPH", "ARTICLE", "SECTION", "DOC", "]]", "\"", "/"};
        for (String removeWord: removeWords) {
            parsedString = parsedString.replace(removeWord, "");
        }

        String[] list = Arrays.stream(parsedString.split("[<|>]"))
                .filter(s -> !s.isEmpty())
                .filter(s -> !s.startsWith("PARAGRAPH"))
                .map(String::trim)
                .toArray(String[]::new);
        list = Arrays.stream(list)
                .filter(s -> !s.startsWith("tagName"))
                .map(String::trim)
                .toArray(String[]::new);

        String[] trimArray = Arrays.stream(Arrays.copyOfRange(list, 1, list.length))
                .filter(s-> !s.isBlank())
                .toArray(String[]:: new);
        String result = String.join("\n", trimArray);
        return result;
    }
}
