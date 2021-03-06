package ru.kontur.elba;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import eu.erikw.PullToRefreshListView;
import ru.kontur.elba.datalayer.LocaleService;
import ru.kontur.elba.domainmodel.Document;
import ru.kontur.elba.domainmodel.DocumentType;

public class DocumentListActivity extends Activity implements AdapterView.OnItemClickListener, PullToRefreshListView.OnRefreshListener {
    private DocumentRepository documentRepository;
    private PullToRefreshListView list;
    private PlainAdapter<Document> adapter;
    private ToggleButton activeTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_list);
        list = (PullToRefreshListView) findViewById(R.id.refreshableList);
        list.setOnItemClickListener(this);
        list.setOnRefreshListener(this);
        list.setEmptyView(findViewById(android.R.id.empty));
        documentRepository = ((ElbaApplication) getApplication()).getBillRepository();
        initTabs();
        refresh(null);
    }

    private void initTabs() {
        ToggleButton[] tabs = ViewHelpers.enumerateButtons((ViewGroup) findViewById(R.id.tabs), ToggleButton.class);
        tabs[0].setChecked(true);
        for (ToggleButton tab : tabs) {
            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activeTab != null)
                        activeTab.setChecked(false);
                    activeTab = (ToggleButton) view;
                    activeTab.setChecked(true);
                    filter(view);
                }
            });
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
        Intent intent = new Intent(this, EditDocumentActivity.class);
        intent.putExtra("documentId", (int) id);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh(null);
    }

    private void refresh(DocumentType type) {
        if (adapter == null) {
            adapter = new PlainAdapter<Document>(this,
                    R.layout.document_list_item,
                    documentRepository.selectByDocumentType(type),
                    new PlainAdapter.ItemViewBinder<Document>() {
                        @Override
                        public void bindItemView(Document item, View itemView) {
                            ((TextView) itemView.findViewById(R.id.date)).setText(LocaleService.getInstance().formatDate(item.date));
                            ((TextView) itemView.findViewById(R.id.sum)).setText(LocaleService.getInstance().formatCurrency(item.sum));
                            ((TextView) itemView.findViewById(R.id.type)).setText(item.type.toString());
                            ((TextView) itemView.findViewById(R.id.number)).setText(item.number);
                            ((TextView) itemView.findViewById(R.id.contractorName)).setText(item.customerName);
                        }
                    });

            list.setAdapter(adapter);
        } else {
            adapter.reload(documentRepository.selectByDocumentType(type));
            adapter.notifyDataSetChanged();
        }
    }

    public void createNew(View view) {
        showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        CharSequence[] items = new CharSequence[]{"Счёт", "Акт", "Накладная"};
        return new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createDocument(i);
                    }
                })
                .create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean b = super.onCreateOptionsMenu(menu);
        for (DocumentType t : DocumentType.values())
            menu.add(Menu.NONE, t.ordinal(), Menu.NONE, t.toString());
        return b;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        createDocument(item.getItemId());
        return true;
    }

    private void createDocument(int documentType) {
        Intent i = new Intent(this, EditDocumentActivity.class);
        i.putExtra("documentType", documentType);
        startActivityForResult(i, 1);
    }

    public void filter(View view) {
        String tag = (String) view.getTag();
        DocumentType type = tag == null ? null : DocumentType.values()[Integer.parseInt(tag)];
        refresh(type);
    }

    @Override
    public void onRefresh() {
    }
}