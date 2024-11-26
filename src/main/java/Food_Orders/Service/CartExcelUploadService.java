package Food_Orders.Service;

import Food_Orders.Entity.Address;
import Food_Orders.Entity.Cart;
import Food_Orders.Entity.CartItem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CartExcelUploadService {

    private static final Logger logger = LoggerFactory.getLogger(CartExcelUploadService.class);

    public static List<Cart> getCartsDataFromExcel(InputStream inputStream) {
        List<Cart> carts = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet("carts");
            if (sheet == null) {
                logger.error("Sheet named 'carts' not found.");
                return carts;
            }

            Iterator<Row> rowIterator = sheet.iterator();
            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (rowIndex < 1) {
                    rowIndex++;
                    continue;
                }

                Cart cart = new Cart();
                Address address = null;
                List<CartItem> cartItems = new ArrayList<>();
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    try {
                        switch (cellIndex) {
                            case 0:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    cart.setId((long) cell.getNumericCellValue());
                                } else if (cell.getCellType() == CellType.STRING) {
                                    cart.setId(Long.parseLong(cell.getStringCellValue().trim()));
                                }
                                break;

                            case 1:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    cart.setTotalAmount(cell.getNumericCellValue());
                                } else if (cell.getCellType() == CellType.STRING) {
                                    cart.setTotalAmount(Double.parseDouble(cell.getStringCellValue().trim()));
                                }
                                break;

                            case 2:
                                if (cell.getCellType() == CellType.STRING) {
                                    cart.setPhoneNumber(cell.getStringCellValue().trim());
                                }
                                break;

                            case 3:
                                if (cell.getCellType() == CellType.STRING) {
                                    cart.setEmail(cell.getStringCellValue().trim());
                                }
                                break;

                            case 4:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    cart.setDate(cell.getDateCellValue());
                                } else if (cell.getCellType() == CellType.STRING) {
                                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cell.getStringCellValue().trim());
                                    cart.setDate(date);
                                }
                                break;

                            case 5:
                                if (address == null) address = new Address();
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    address.setId((long) cell.getNumericCellValue());
                                } else if (cell.getCellType() == CellType.STRING) {
                                    address.setId(Long.parseLong(cell.getStringCellValue().trim()));
                                }
                                break;

                            case 6:
                                if (cell.getCellType() == CellType.STRING && address != null) {
                                    address.setHouseNumber(cell.getStringCellValue().trim());
                                }
                                break;

                            case 7:
                                if (cell.getCellType() == CellType.STRING && address != null) {
                                    address.setLandMark(cell.getStringCellValue().trim());
                                }
                                break;

                            case 8:
                                if (cell.getCellType() == CellType.STRING && address != null) {
                                    address.setStreet(cell.getStringCellValue().trim());
                                }
                                break;

                            case 9:
                                if (cell.getCellType() == CellType.STRING && address != null) {
                                    address.setCity(cell.getStringCellValue().trim());
                                }
                                break;

                            case 10:
                                if (cell.getCellType() == CellType.STRING && address != null) {
                                    address.setState(cell.getStringCellValue().trim());
                                }
                                break;

                            case 11:
                                if (cell.getCellType() == CellType.STRING && address != null) {
                                    address.setZipCode(cell.getStringCellValue().trim());
                                }
                                break;

                            case 12:
                                if (cell.getCellType() == CellType.NUMERIC) {
                                    CartItem cartItem = new CartItem();
                                    cartItem.setId((long) cell.getNumericCellValue());
                                    cartItems.add(cartItem);
                                }
                                break;

                            case 13:
                                if (cell.getCellType() == CellType.STRING && !cartItems.isEmpty()) {
                                    cartItems.get(cartItems.size() - 1).setName(cell.getStringCellValue().trim());
                                }
                                break;

                            case 14:
                                if (cell.getCellType() == CellType.NUMERIC && !cartItems.isEmpty()) {
                                    cartItems.get(cartItems.size() - 1).setPrice(cell.getNumericCellValue());
                                }
                                break;

                            case 15:
                                if (cell.getCellType() == CellType.NUMERIC && !cartItems.isEmpty()) {
                                    cartItems.get(cartItems.size() - 1).setQuantity((int) cell.getNumericCellValue());
                                }
                                break;

                            case 16:
                                if (cell.getCellType() == CellType.NUMERIC && !cartItems.isEmpty()) {
                                    cartItems.get(cartItems.size() - 1).setTotalGST(cell.getNumericCellValue());
                                }
                                break;

                            default:
                                logger.warn("Unexpected cell index: " + cellIndex);
                        }
                    } catch (Exception ex) {
                        logger.error("Error processing cell at row " + rowIndex + ", column " + cellIndex, ex);
                    }
                    cellIndex++;
                }

                if (address != null) {
                    cart.setAddress(address);
                }
                cart.setCartItems(cartItems);

                carts.add(cart);
                rowIndex++;
            }
        } catch (Exception e) {
            logger.error("Error reading Excel file", e);
        }

        return carts;
    }

    public static boolean isValidExcelFile(MultipartFile file) {
        return file != null && file.getOriginalFilename().endsWith(".xlsx");
    }
}
