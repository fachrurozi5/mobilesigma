package com.fachru.sigmamobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.fragment.HeaderSOFragment;
import com.fachru.sigmamobile.fragment.PrepareSOFragment;
import com.fachru.sigmamobile.fragment.SalesOrderFragment;
import com.fachru.sigmamobile.fragment.interfaces.OnSetSoHeadListener;
import com.fachru.sigmamobile.fragment.interfaces.OnSetSoItemListener;
import com.fachru.sigmamobile.model.Customer;
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.Employee;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.SoHead;
import com.fachru.sigmamobile.model.SoItem;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesOrderActivity extends AppCompatActivity implements
        OnTabSelectedListener, OnSetSoHeadListener, OnSetSoItemListener {

    private static final String TAG = "SalesOrderActivity";


    /*
    * menu
    * */
    protected MenuItem action_done;

    /*
    * widget
    * */
    protected Toolbar toolbar;
    protected TabLayout tabLayout;
    /*
    * label
    * */
    protected long total = 0;
    protected long bonus = 0;
    protected long neto = 0;
    protected long ppn = 0;
    protected long grand_total = 0;
    /*
    * plain old java object
    * */
    SoHead soHead;
    private long custid;
    private long emplid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order);
        initComp();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        custid = intent.getLongExtra(CustomerActivity.CUSTID, -1);
        emplid = intent.getLongExtra(Login.EMPLID, -1);
        fragmentPosition(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sales_order, menu);
        action_done = menu.findItem(R.id.action_done);
        action_done.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_done) {
            Log.i(TAG, "Action Done Click");
            for (SoItem doItem : soHead.soItems()) {
                total += doItem.subTotal;
            }
            Log.e(TAG, soHead.toString());
            showDialogOrder();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        fragmentPosition(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void initComp() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order), true);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order_product), false);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_prepare), false);
        tabLayout.setOnTabSelectedListener(this);

    }

    private void fragmentPosition(int position) {
        FragmentTransaction fragmentTransaction;
        Fragment fragment = null;
        Bundle bundle;
        switch (position) {
            case 0:
                fragment = new HeaderSOFragment();
                ((HeaderSOFragment) fragment).setOnSoHeadListener(this);
                bundle = new Bundle();
                bundle.putLong(CustomerActivity.CUSTID, custid);
                bundle.putLong(Login.EMPLID, emplid);
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new SalesOrderFragment();
                ((SalesOrderFragment) fragment).setOnSoItemListener(this);
                if (soHead != null) {
                    bundle = new Bundle();
                    bundle.putString(Constanta.KEY_SO, soHead.so);
                    bundle.putString(Constanta.KEY_WHID, soHead.whid);
                    fragment.setArguments(bundle);
                }
                break;
            case 2:
                fragment = new PrepareSOFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment).commit();
        }
    }

    private void showDialogOrder() {

        bonus = Discount.getDiscountValAsString(total);
        neto = total - bonus;
        ppn = (neto * 10) / 100;
        grand_total = neto + ppn;

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.title_dialog_order)
                .customView(R.layout.prompt_order_done, true)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        soHead.vatamt = ppn;
                        soHead.netamt = grand_total;
                        soHead.uploaded = false;
                        soHead.date_order = new Date();
                        int i = 1;
                        for (SoItem item : soHead.soItems()) {
                            item.noItem = String.format("%04d", i++);
                            Log.e(TAG, item.toString());
                            item.save();
                        }
                        soHead.printed = true;
                        soHead.save();
                        Log.d(TAG, "sohead Saved");
                        Log.d(TAG, soHead.toString());
                        actionPrint(soHead.so);
                        tabLayout.getTabAt(0).select();
                    }
                }).build();
        EditText et_total = (EditText) dialog.getCustomView().findViewById(R.id.et_total);
        EditText et_bonus = (EditText) dialog.getCustomView().findViewById(R.id.et_bonus);
        EditText et_ppn = (EditText) dialog.getCustomView().findViewById(R.id.et_ppn);
        final EditText et_neto = (EditText) dialog.getCustomView().findViewById(R.id.et_neto);
        final EditText et_bayar = (EditText) dialog.getCustomView().findViewById(R.id.et_bayar);
        final EditText et_kembali = (EditText) dialog.getCustomView().findViewById(R.id.et_kembali);
        final EditText et_grand_total = (EditText) dialog.getCustomView().findViewById(R.id.et_grand_total);

        et_total.setText(CommonUtil.priceFormat2Decimal(total));
        et_bonus.setText(CommonUtil.priceFormat2Decimal(bonus));
        et_neto.setText(CommonUtil.priceFormat2Decimal(neto));
        et_ppn.setText(CommonUtil.priceFormat2Decimal(ppn));
        et_grand_total.setText(CommonUtil.priceFormat2Decimal(grand_total));

        et_bayar.setVisibility(View.GONE);
        et_kembali.setVisibility(View.GONE);

        /*et_bonus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    bonus = Long.parseLong(s.toString());
                    neto = total - bonus;
                    grand_total = neto + ppn;
                    et_neto.setText(CommonUtil.priceFormat2Decimal(neto));
                    et_grand_total.setText(CommonUtil.priceFormat2Decimal(grand_total));
                } catch (Exception e) {
                    e.printStackTrace();
                    et_neto.getText().clear();
                    et_grand_total.getText().clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        dialog.show();
    }

    @Override
    public void onSetSoHead(SoHead soHead) {
        this.soHead = soHead;
        // TODO: 24/03/16 delete onsetsohead
        Log.i(TAG, "SoHead set");
        Log.d(TAG, soHead.toString());
        tabLayout.getTabAt(1).select();
        action_done.setVisible(true);
    }

    @Override
    public void unSetSoHead() {
        // TODO: 24/03/16 delete unsetsohead
        Log.i(TAG, "unset SoHead");
    }

    @Override
    public void unSetDoItem() {
        // TODO: 24/03/16 deltee unsetdoitem
        soHead = null;
        Log.i(TAG, "unset doitem");
        action_done.setVisible(false);
    }

    private void actionPrint(String title) {

        Document document = new Document();
        File file = null;

        try {

            PdfPTable table = createTable();

            createHeaderSOTable(table);

            createColumnsTableTitle(table);

            fillTable(table);

            document = new Document(new Rectangle(612, table.getTotalHeight() + 250));

            file = CommonUtil.getOutputMediaFile(title);

            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(document, fOut);

            document.open();

            document.add(table);

            document.add(createFooter());

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            document.close();
            total = bonus = 0;
            if (file != null)
                openPDf(file);
        }
    }

    private PdfPTable createTable() {
        PdfPTable table = new PdfPTable(5);
        table.setTotalWidth(400);
        table.setLockedWidth(true);

        return table;
    }

    public void createHeaderSOTable(PdfPTable table) {

        Customer customer = Customer.getCustomer(soHead.custid);

        SimpleDateFormat format = new SimpleDateFormat("hh/MM/yyyy");

        table.addCell(createCell("No. Order:", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 0));
        table.addCell(createCell(soHead.so, Element.ALIGN_LEFT, Rectangle.NO_BORDER, 2));
        table.addCell(createCell("Tgl. Order:", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 0));
        table.addCell(createCell(format.format(soHead.so_date), Element.ALIGN_LEFT, Rectangle.NO_BORDER, 2));

        table.addCell(createCell("No. PO:", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 0));
        table.addCell(createCell(soHead.purchase_order, Element.ALIGN_LEFT, Rectangle.NO_BORDER, 2));
        table.addCell(createCell("Tgl. Kirim:", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 0));
        table.addCell(createCell(format.format(soHead.delivery_date), Element.ALIGN_LEFT, Rectangle.NO_BORDER, 2));

        table.addCell(createCell("Tgl. Cetak:", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 0));
        table.addCell(createCell(new SimpleDateFormat("hh/MM/yyyy HH:mm:ss.SSS").format(new Date()), Element.ALIGN_LEFT, Rectangle.NO_BORDER, 4));

        table.addCell(createCell("NPWP", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 1));
        table.addCell(createCell(customer.taxid, Element.ALIGN_LEFT, Rectangle.NO_BORDER, 4));
        table.addCell(createCell("Alamat Pengiriman", Element.ALIGN_LEFT, Rectangle.NO_BORDER, 5));
        table.addCell(createCell(customer.custid + " " + customer.name, Element.ALIGN_LEFT, Rectangle.NO_BORDER, 5));
        table.addCell(createCell(customer.deladd1, Element.ALIGN_LEFT, Rectangle.NO_BORDER, 5));
        table.addCell(createCell(customer.deladd2, Element.ALIGN_LEFT, Rectangle.NO_BORDER, 5));
        table.addCell(createCell(customer.deladd3, Element.ALIGN_LEFT, Rectangle.NO_BORDER, 5));
        table.addCell(createCell(customer.deladd4, Element.ALIGN_LEFT, Rectangle.NO_BORDER, 5));

    }

    public void createColumnsTableTitle( PdfPTable table) {

        table.addCell(createCell("Kode", Element.ALIGN_CENTER, Rectangle.NO_BORDER, 0));
        table.addCell(createCell("Nama Produk", Element.ALIGN_CENTER, Rectangle.NO_BORDER, 4));
        table.addCell(createCell("Satuan", Element.ALIGN_CENTER, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Qty", Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Harga/Unit", Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Disc", Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Jml Bersih", Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
    }

    public void fillTable(PdfPTable table) {

        Product product;
        for (SoItem item : soHead.soItems()) {
            product = Product.find(item.productId);
            table.addCell(createCell(product.prodid, Element.ALIGN_CENTER, Rectangle.NO_BORDER, 1));
            table.addCell(createCell(product.name, Element.ALIGN_LEFT, Rectangle.NO_BORDER, 4));
            table.addCell(createCell("PCS", Element.ALIGN_CENTER, Rectangle.NO_BORDER, 1));
            table.addCell(createCell(String.valueOf(item.qty),Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
//            table.addCell(createCell(getProductPriceAsString(product, soHead.priceType), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell(CommonUtil.priceFormat(item.priceList), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell(CommonUtil.percentFormat(item.discountPrinc + item.discountNst), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell(CommonUtil.priceFormat(item.subTotal), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
        }


        table.addCell(createCell("Total Sebelum PPN :", Element.ALIGN_RIGHT, Rectangle.TOP, 4));
        table.addCell(createCell(CommonUtil.priceFormat(total), Element.ALIGN_RIGHT, Rectangle.TOP, 1));
        table.addCell(createCell("Bonus :", Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
        table.addCell(createCell(CommonUtil.priceFormat(bonus), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
        table.addCell(createCell("Dasar Pengenaan Pajak :", Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
        table.addCell(createCell(CommonUtil.priceFormat(total), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
        table.addCell(createCell("PPN(10%) :", Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
        table.addCell(createCell(CommonUtil.priceFormat(soHead.vatamt), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
        table.addCell(createCell("Grand Total :", Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
        table.addCell(createCell(CommonUtil.priceFormat(soHead.netamt), Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));

    }

    public PdfPTable createFooter() {

        Employee employee = Employee.getEmployee(soHead.empid);

        float[] columnWidths = {2f, 0.8f, 2f};
        PdfPTable table = new PdfPTable(columnWidths);
        table.setTotalWidth(400);
        table.setLockedWidth(true);
        table.setSpacingBefore(100f);

        table.addCell(createCell(employee.employee_id + "\n" + employee.name, Element.ALIGN_LEFT, Rectangle.TOP, 0));
        table.addCell(createCell("", Element.ALIGN_CENTER, Rectangle.NO_BORDER, 0));
        table.addCell(createCell("Ttd. Pelanggan", Element.ALIGN_LEFT, Rectangle.TOP, 0));

        return table;
    }

    private PdfPCell createCell(String text,int align, int border, int colspan) {
        PdfPCell pdfPCel = new PdfPCell(Phrase.getInstance(text));
        pdfPCel.setHorizontalAlignment(align);
        pdfPCel.setBorder(border);
        pdfPCel.setColspan(colspan);
        pdfPCel.setPaddingBottom(5f);
        return pdfPCel;
    }

    private String getProductPriceAsString(Product product, int type_price_list) {
        String product_price = "";

        switch (type_price_list) {
            case 1:
                product_price = CommonUtil.priceFormat(product.po_price);
                break;
            case 2:
                product_price = CommonUtil.priceFormat(product.sellprice);
                break;
            case 3:
                product_price = CommonUtil.priceFormat(product.base_price);
                break;
            case 4:
                product_price = CommonUtil.priceFormat(product.old_price);
                break;
            case 5:
                product_price = CommonUtil.priceFormat(product.test_price);
                break;
        }

        return product_price;
    }

    private void openPDf(File file) {
        if (CommonUtil.canDisplayPdf(getApplicationContext())) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            startActivity(intent);
        } else {
            new MaterialDialog.Builder(this)
                    .title("Information")
                    .content("there's not pdf reader in this device, please install them")
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.adobe.reader")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.adobe.reader")));
                            }
                        }
                    })
                    .show();
        }
    }
}
