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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fachru.sigmamobile.fragment.DoneOrderFragment;
import com.fachru.sigmamobile.fragment.HeaderPOSFragment;
import com.fachru.sigmamobile.fragment.PointOfSaleFragment;
import com.fachru.sigmamobile.fragment.PointOfSaleFragment.OnSetDoItemListener;
import com.fachru.sigmamobile.fragment.interfaces.OnSetDoHeadListener;
import com.fachru.sigmamobile.model.Discount;
import com.fachru.sigmamobile.model.DoHead;
import com.fachru.sigmamobile.model.DoItem;
import com.fachru.sigmamobile.model.Product;
import com.fachru.sigmamobile.model.UnitConverter;
import com.fachru.sigmamobile.model.WarehouseStock;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class PointOfSaleActivity extends AppCompatActivity implements
        OnTabSelectedListener, OnSetDoHeadListener, OnSetDoItemListener {

    protected TabLayout tabLayout;
    protected DoHead doHead;

    /*
    * menu
    * */
    protected MenuItem action_done;

    /*
    * label
    * */
    protected long total = 0;
    protected long bonus = 0;
    protected long neto = 0;
    protected long ppn = 0;
    protected long grand_total = 0;
    protected long bayar = 0;
    protected long kembali = 0;
    private long custid;
    private long emplid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_sale);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initComp();
        Intent intent = getIntent();
        custid = intent.getLongExtra(CustomerActivity.CUSTID, -1);
        emplid = intent.getLongExtra(Login.EMPLID, -1);
        fragmentPosition(0);
    }

    protected void initComp() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order), true);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order_product), false);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_order_done), false);
        tabLayout.setOnTabSelectedListener(this);
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
            for (DoItem doItem : doHead.doItems()) {
                total += doItem.sub_total;
            }
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

    @Override
    public void onSetDoHead(DoHead doHead) {
        this.doHead = doHead;
        tabLayout.getTabAt(1).select();
        action_done.setVisible(true);
    }

    @Override
    public void unSetDoHead() {
    }

    @Override
    public void unSetDoItem() {
        doHead = null;
        action_done.setVisible(false);
    }

    private void fragmentPosition(int position) {
        FragmentTransaction fragmentTransaction;
        Fragment fragment = null;
        Bundle bundle;
        switch (position) {
            case 0:
                fragment = new HeaderPOSFragment();
                ((HeaderPOSFragment) fragment).setOnSetDoHeadListener(this);
                bundle = new Bundle();
                bundle.putLong(CustomerActivity.CUSTID, custid);
                bundle.putLong(Login.EMPLID, emplid);
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new PointOfSaleFragment();
                ((PointOfSaleFragment) fragment).setOnDoItemListener(this);
                if (doHead != null) {
                    bundle = new Bundle();
                    bundle.putString(Constanta.KEY_DOC_NO, doHead.doc_no);
                    bundle.putString(Constanta.KEY_WHID, doHead.whid);
                    fragment.setArguments(bundle);
                }
                break;
            case 2:
                fragment = new DoneOrderFragment();
                bundle = new Bundle();
                bundle.putString(Constanta.KEY_DOC_NO, Constanta.KEY_DOC_NO);
                fragment.setArguments(bundle);
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
                        doHead.vatamt = ppn;
                        doHead.netamt = grand_total;
                        doHead.dibayar = bayar;
                        doHead.docprint = 1;
                        doHead.uploaded = false;
                        doHead.doc_date = new Date();
                        int i = 1;
                        for (DoItem item : doHead.doItems()) {
                            item.noitem = String.format("%04d", i++);
                            WarehouseStock stock = WarehouseStock.findById(doHead.whid, item.product_id);
                            Product product = Product.find(item.product_id);
                            UnitConverter unitConverter = UnitConverter.find(product.unitid, item.unit_id);
                            if (unitConverter == null) {
                                stock.balance -= item.qty;
                            } else {
                                double balance = (stock.balance * unitConverter.factor) - item.qty;
                                stock.balance = balance / unitConverter.factor;
                            }

                            stock.save();
                            item.save();
                        }
                        actionPrint(doHead.doc_no);
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

        et_bonus.addTextChangedListener(new TextWatcher() {
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
        });

        et_bayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    bayar = Long.parseLong(s.toString());
                    kembali = bayar - grand_total;
                    et_kembali.setText(CommonUtil.priceFormat2Decimal(kembali));
                } catch (Exception e) {
                    e.printStackTrace();
                    et_kembali.getText().clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog.show();
    }


    private void actionPrint(String title) {
        Rectangle rectangle = new Rectangle(216f, 720f);
        Document doc = new Document(rectangle, 36f, 72f, 108f, 180f); // pagesize,
        File file = null;

        try {

            file = CommonUtil.getOutputMediaFile(title);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter docWriter = PdfWriter.getInstance(doc, fOut);

            doc.open();

            float[] columnWidths = {1f, 0.8f, 2f, 1.5f, 1.5f};

            Font font = FontFactory.getFont("Times-Roman", 6, Font.NORMAL);

            PdfPTable table = createTable(columnWidths, 150f, font);

            Product product = null;
            for (DoItem item : doHead.doItems()) {
                product = Product.find(item.product_id);
                table.addCell(createCell(product.prodid, font, Element.ALIGN_CENTER, Rectangle.NO_BORDER, 1));
                table.addCell(createCell(product.name, font, Element.ALIGN_LEFT, Rectangle.NO_BORDER, 4));
                table.addCell(createCell(item.unit_id, font, Element.ALIGN_CENTER, Rectangle.NO_BORDER, 1));
                table.addCell(createCell(String.valueOf(item.qty), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
                table.addCell(createCell(CommonUtil.priceFormat(item.pricelist), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
                table.addCell(createCell(CommonUtil.percentFormat(/*item.discount_principal + item.discount_nusantara*/0), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
                table.addCell(createCell(CommonUtil.priceFormat(item.sub_total), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            }

            table.addCell(createCell("Total Sebelum PPN :", font, Element.ALIGN_RIGHT, Rectangle.TOP, 4));
            table.addCell(createCell(CommonUtil.priceFormat(total), font, Element.ALIGN_RIGHT, Rectangle.TOP, 1));
            table.addCell(createCell("Bonus :", font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
            table.addCell(createCell(CommonUtil.priceFormat(bonus), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell("Dasar Pengenaan Pajak :", font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
            table.addCell(createCell(CommonUtil.priceFormat(total), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell("PPN(10%) :", font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
            table.addCell(createCell(CommonUtil.priceFormat(doHead.vatamt), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell("Grand Total :", font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
            table.addCell(createCell(CommonUtil.priceFormat(doHead.netamt), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));

            table.writeSelectedRows(0, -1, doc.leftMargin(), 650, docWriter.getDirectContent());

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
            doHead.docprint = 1;
            doHead.save();
            total = bonus = 0;
            if (file != null)
                openPDf(file);
        }
    }


    private PdfPTable createTable(float[] columnWidths, float maxWidth, Font font) {

        PdfPTable table = new PdfPTable(columnWidths);


        table.setTotalWidth(maxWidth);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        table.addCell(createCell("Kode", font, Element.ALIGN_CENTER, Rectangle.NO_BORDER, 1));
        table.addCell(createCell("Nama Produk", font, Element.ALIGN_CENTER, Rectangle.NO_BORDER, 4));
        table.addCell(createCell("Satuan", font, Element.ALIGN_CENTER, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Qty", font, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Harga/Unit", font, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Disc", font, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
        table.addCell(createCell("Jml Bersih", font, Element.ALIGN_RIGHT, Rectangle.BOTTOM, 0));
        table.setHeaderRows(1);

        return table;
    }

    private PdfPCell createCell(String text, Font font, int align, int border, int colspan) {
        PdfPCell pdfPCel = new PdfPCell(new Phrase(text, font));
        pdfPCel.setHorizontalAlignment(align);
        pdfPCel.setBorder(border);
        pdfPCel.setColspan(colspan);
        return pdfPCel;
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

    /*private void openPDf(File file) {
        *//*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        startActivity(intent);*//*
    }*/
}
