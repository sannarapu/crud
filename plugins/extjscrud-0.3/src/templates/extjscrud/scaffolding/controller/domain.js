<% import grails.persistence.Event %>

Ext.define('AM.controller.domain', {
    extend:'Ext.app.Controller',

    stores:['domains'],

    views:['Viewport'],

    init:function () {
        this.control({
            'treepanel': {

                itemclick: function (view, record, item, index, e) {

                    var clickedMnuNode = record;
                    // Open tab only if the menu node is a leaf node
                    if (clickedMnuNode.isLeaf()) {

                        var parentPanel =  view.up('viewport');
                        var tabPanelItem = parentPanel.down('tabpanel');
                        var cmpId = clickedMnuNode.get('text');
                        var panel = tabPanelItem.down(cmpId);
                        if(panel) {
                            tabPanelItem.setActiveTab(panel);
                        }else{
                            var newTab = tabPanelItem.add({
                                stateful: true,
                                stateId: cmpId,
                                xtype : cmpId,
                                closable: true,
                                listeners: {
                                    beforeclose: function(panel,opts){
                                        this.ownerCt.remove(panel);
                                    }
                                }
                            });
                            tabPanelItem.setActiveTab(newTab);
                            tabPanelItem.doLayout();
                        }

                    }
                }
            }

        });

        this.callParent();
    }


});
