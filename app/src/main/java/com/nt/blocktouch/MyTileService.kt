package com.nt.blocktouch

import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class MyTileService: TileService() {
    override fun onTileAdded() {
        super.onTileAdded()

        // Update state
        qsTile.state = Tile.STATE_INACTIVE

        // Update looks
        qsTile.updateTile()
    }


    override fun onClick() {
        super.onClick()
        if(qsTile.state == Tile.STATE_INACTIVE) {
            // Turn on
            qsTile.state = Tile.STATE_ACTIVE
            val intent = Intent(this, FloatingViewService::class.java)
            intent.putExtra("lock_state", "open");
            startService(intent)


        } else {
            // Turn off
            qsTile.state = Tile.STATE_INACTIVE
            val intent = Intent(this, FloatingViewService::class.java)
            intent.putExtra("lock_state", "close");
            startService(intent)
        }

        // Update looks
        qsTile.updateTile()
    }

}