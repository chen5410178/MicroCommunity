<template>
  <div>
    <el-row>
      <el-col :span="24" class="text-right"></el-col>
    </el-row>
    <div class="margin-top">
      <el-table :data="simplifyShopOwnerInfo.owners" border stripe>
        <el-table-column :label="$t('simplifyShopOwner.ownerFace')" align="center">
          <template slot-scope="scope">
            <el-image 
              style="width: 60px; height: 60px; border-radius: 4px;"
              :src="scope.row.url || '/img/noPhoto.jpg'"
              :preview-src-list="[scope.row.url]"
              v-if="scope.row.url"
            ></el-image>
            <el-image 
              style="width: 60px; height: 60px; border-radius: 4px;"
              src="/img/noPhoto.jpg"
              v-else
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column :label="$t('simplifyShopOwner.name')" align="center">
          <template slot-scope="scope">
            {{ scope.row.name }}({{ scope.row.link }})
          </template>
        </el-table-column>
        <el-table-column :label="$t('simplifyShopOwner.gender')" align="center">
          <template slot-scope="scope">
            {{ scope.row.sex == 0 ? $t('simplifyShopOwner.male') : $t('simplifyShopOwner.female') }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" :label="$t('simplifyShopOwner.idCard')" align="center">
          <template slot-scope="scope">
            {{ scope.row.idCard || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="address" :label="$t('simplifyShopOwner.address')" align="center">
          <template slot-scope="scope">
            {{ scope.row.address || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="roomCount" :label="$t('simplifyShopOwner.roomCount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.roomCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="memberCount" :label="$t('simplifyShopOwner.memberCount')" align="center">
          <template slot-scope="scope">
            {{ scope.row.memberCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="oweFee" :label="$t('simplifyShopOwner.oweFee')" align="center">
          <template slot-scope="scope">
            {{ scope.row.oweFee || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('simplifyShopOwner.actions')" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="_toOwnerDetail(scope.row)">
              {{ $t('simplifyShopOwner.detail') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryOwners } from '@/api/fee/carDetailOwnerApi'
import { getShopOwnerInfo } from '@/api/shop/shopCreateFeeApi'

export default {
  name: 'SimplifyShopOwner',
  data() {
    return {
      simplifyShopOwnerInfo: {
        owners: [],
        ownerId: ''
      }
    }
  },
  created() {
    this.communityId = getCommunityId()
  },
  methods: {
    open(params) {
      this.handleSwitch(params)
    },
    handleSwitch(param) {
      if (!param.shopId) {
        return
      }
      this.loadShopOwner(param.shopId)
    },
    loadShopOwner(shopId) {
      getShopOwnerInfo(shopId).then(res => {
        const owners = res.data || []
        if (owners.length > 0) {
          const owner = owners[0]
          this.loadOwnerDetail(owner.ownerId)
        } else {
          this.simplifyShopOwnerInfo.owners = []
        }
      }).catch(error => {
        console.error('Failed to load shop owner:', error)
        this.simplifyShopOwnerInfo.owners = []
      })
    },
    loadOwnerDetail(ownerId) {
      queryOwners({
        ownerId: ownerId,
        communityId: this.communityId,
        ownerTypeCd: '1001',
        page: 1,
        row: 10
      }).then(res => {
        this.simplifyShopOwnerInfo.owners = res.data || []
      }).catch(error => {
        console.error('Failed to load owner detail:', error)
      })
    },
    _toOwnerDetail(owner) {
      window.open(`/#/views/owner/ownerDetail?ownerId=${owner.ownerId}`)
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 15px;
}
.text-right {
  text-align: right;
}
</style>
