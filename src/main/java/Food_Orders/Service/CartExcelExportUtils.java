package Food_Orders.Service;

import Food_Orders.Entity.Cart;
import Food_Orders.Entity.CartItem;
import Food_Orders.Entity.Address;
import Food_Orders.Repository.CartItemRepository;
import Food_Orders.Repository.AddressRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class CartExcelExportUtils {

    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<Cart> cartList;

    public CartExcelExportUtils(List<Cart> carts, CartItemRepository cartItemRepository, AddressRepository addressRepository, XSSFWorkbook workbook) {
        this.workbook = workbook;
        this.cartList = carts;
    }
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }

    private void createHeaderRow() {
        sheet = workbook.createSheet("carts");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        createCell(row, 0, "Cart Details Information", style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
        font.setFontHeightInPoints((short) 10);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(12);
        style.setFont(font);

        createCell(row, 0, "Cart ID", style);
        createCell(row, 1, "Total Amount", style);
        createCell(row, 2, "Phone Number", style);
        createCell(row, 3, "Email", style);
        createCell(row, 4, "Date", style);

        // Address fields
        createCell(row, 5, "Address ID", style);
        createCell(row, 6, "House Number", style);
        createCell(row, 7, "Landmark", style);
        createCell(row, 8, "Street", style);
        createCell(row, 9, "City", style);
        createCell(row, 10, "State", style);
        createCell(row, 11, "Zip Code", style);

        // CartItem fields
        createCell(row, 12, "Cart Item ID", style);
        createCell(row, 13, "Item Name", style);
        createCell(row, 14, "Item Price", style);
        createCell(row, 15, "Item Quantity", style);
        createCell(row, 16, "Item Total GST", style);
    }

    private void writeCartData() {
        int rowCount = 2;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(10);
        style.setFont(font);

        for (Cart cart : cartList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, cart.getId(), style);
            createCell(row, columnCount++, cart.getTotalAmount(), style);
            createCell(row, columnCount++, cart.getPhoneNumber(), style);
            createCell(row, columnCount++, cart.getEmail(), style);
            createCell(row, columnCount++, cart.getDate(), style);

            Address address = cart.getAddress();
            if (address != null) {
                createCell(row, columnCount++, address.getId(), style);
                createCell(row, columnCount++, address.getHouseNumber(), style);
                createCell(row, columnCount++, address.getLandMark(), style);
                createCell(row, columnCount++, address.getStreet(), style);
                createCell(row, columnCount++, address.getCity(), style);
                createCell(row, columnCount++, address.getState(), style);
                createCell(row, columnCount++, address.getZipCode(), style);
            } else {

                for (int i = 0; i < 7; i++) {
                    createCell(row, columnCount++, "", style);
                }
            }


            List<CartItem> cartItems = cart.getCartItems();
            for (CartItem item : cartItems) {
                createCell(row, columnCount++, item.getId(), style);
                createCell(row, columnCount++, item.getName(), style);
                createCell(row, columnCount++, item.getPrice(), style);
                createCell(row, columnCount++, item.getQuantity(), style);
                createCell(row, columnCount++, item.getTotalGST(), style);
            }
        }
    }

    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        createHeaderRow();
        writeCartData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
