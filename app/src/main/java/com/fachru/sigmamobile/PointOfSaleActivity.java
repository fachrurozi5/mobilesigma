package com.fachru.sigmamobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
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
import com.fachru.sigmamobile.model.model.DoHead;
import com.fachru.sigmamobile.model.model.DoItem;
import com.fachru.sigmamobile.model.model.Product;
import com.fachru.sigmamobile.utils.CommonUtil;
import com.fachru.sigmamobile.utils.Constanta;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PointOfSaleActivity extends AppCompatActivity implements
        OnTabSelectedListener, OnSetDoHeadListener, OnSetDoItemListener{

    protected static final String TAG_DO_HEAD = "doheadtag";
    protected static final String TAG_DO_ITEM = "doitemtag";
    protected static final String TAG_DONE_ORDER = "doneoredertag";
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
    protected long grand_total = 0;
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
        emplid = intent.getLongExtra(MainActivity.EMPLID, -1);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        } else if (id == R.id.action_done) {
            for(DoItem doItem : doHead.doItems())
            {
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
        switch (position) {
            case 0:
                HeaderPOSFragment fragment = new HeaderPOSFragment();
                fragment.setOnSetDoHeadListener(this);
                Bundle bundle = new Bundle();
                bundle.putLong(CustomerActivity.CUSTID, custid);
                bundle.putLong(MainActivity.EMPLID, emplid);
                fragment.setArguments(bundle);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment, TAG_DO_HEAD).commit();
                break;
            case 1:
                PointOfSaleFragment orderFragment = new PointOfSaleFragment();
                orderFragment.setOnDoItemListener(this);
                if (doHead != null) {
                    bundle = new Bundle();
                    bundle.putString(Constanta.KEY_DOC_NO, doHead.docno);
                    orderFragment.setArguments(bundle);
                }
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, orderFragment, TAG_DO_ITEM).commit();
                break;
            case 2:
                DoneOrderFragment doneOrderFragment = new DoneOrderFragment();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, doneOrderFragment, TAG_DONE_ORDER).commit();
            default:
                break;
        }
    }

    private void showDialogOrder() {

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.title_dialog_order)
                .customView(R.layout.prompt_order_done, true)
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        doHead.total = total;
                        doHead.bonus = bonus;
                        doHead.grand_total = grand_total;
                        doHead.status = true;
                        for (DoItem item : doHead.doItems()) {
                            Product product = item.product;
                            product.stock -= item.jumlah_order;
                            product.save();
                        }
                        doHead.save();
                        actionPrint(doHead.docno);
                        tabLayout.getTabAt(0).select();
                    }
                }).build();
        EditText et_total = (EditText) dialog.getCustomView().findViewById(R.id.et_total);
        EditText et_bonus = (EditText) dialog.getCustomView().findViewById(R.id.et_bonus);
        final EditText et_grand_total = (EditText) dialog.getCustomView().findViewById(R.id.et_grand_total);
        et_total.setText(CommonUtil.priceFormat2Decimal(total));
        et_bonus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    bonus = Long.parseLong(s.toString());
                    grand_total = total - bonus;
                    et_grand_total.setText(CommonUtil.priceFormat2Decimal(grand_total));
                } catch (Exception e ) {
                  e.printStackTrace();
                    et_grand_total.getText().clear();
                }
            }

            @Override
            public void afterTextChanged (Editable s){

            }
        });

        dialog.show();
    }


    private void actionPrint(String title) {
        Rectangle rectangle = new Rectangle(216f, 720f);
        Document doc = new Document(rectangle, 36f, 72f, 108f, 180f); // pagesize,
        File file = null;

        try {
           /* String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SIGMA";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, title + ".pdf");*/

            file = CommonUtil.getOutputMediaFile(title);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter docWriter = PdfWriter.getInstance(doc, fOut);

            doc.open();

            float[] columnWidths = {1f, 0.8f, 2f, 1.5f,1.5f};

            Font font = FontFactory.getFont("Times-Roman", 6, Font.NORMAL);

            PdfPTable table = createTable(columnWidths, 150f, font);

            for (DoItem item : doHead.doItems()) {
                table.addCell(createCell(item.product.product_id, font, Element.ALIGN_CENTER, Rectangle.NO_BORDER, 1));
                table.addCell(createCell(item.product.product_name, font, Element.ALIGN_LEFT, Rectangle.NO_BORDER, 4));
                table.addCell(createCell("PCS", font, Element.ALIGN_CENTER, Rectangle.NO_BORDER, 1));
                table.addCell(createCell(String.valueOf(item.jumlah_order), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
                table.addCell(createCell(CommonUtil.priceFormat(item.product.price), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
                table.addCell(createCell(CommonUtil.percentFormat(item.discount_principal + item.discount_nusantara), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
                table.addCell(createCell(CommonUtil.priceFormat(item.sub_total), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            }

            table.addCell(createCell("Total Sebelum PPN :", font, Element.ALIGN_RIGHT, Rectangle.TOP, 4));
            table.addCell(createCell(CommonUtil.priceFormat(doHead.total), font, Element.ALIGN_RIGHT, Rectangle.TOP, 1));
            table.addCell(createCell("Bonus :", font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
            table.addCell(createCell(CommonUtil.priceFormat(doHead.bonus), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell("Dasar Pengenaan Pajak :", font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
            table.addCell(createCell(CommonUtil.priceFormat(doHead.total), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell("PPN(10%) :", font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
            table.addCell(createCell(CommonUtil.priceFormat((doHead.total * 10) / 100), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));
            table.addCell(createCell("Grand Total :", font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 4));
            table.addCell(createCell(CommonUtil.priceFormat(doHead.grand_total + ((doHead.total * 10) / 100)), font, Element.ALIGN_RIGHT, Rectangle.NO_BORDER, 1));

            table.writeSelectedRows(0, -1, doc.leftMargin(), 650, docWriter.getDirectContent());
            
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally
        {
            doc.close();
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
        Intent intent = new Intent(Intent.ACTION_VIEW);
        /*String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SIGMA";

        File file = new File(path, title + ".pdf");
*/
        intent.setDataAndType( Uri.fromFile(file), "application/pdf" );
        startActivity(intent);
    }
}
