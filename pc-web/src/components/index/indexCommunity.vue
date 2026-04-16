<template>
  <div class="index-1-left-1">
    <div class="index-title">
      <span>{{ $t('propertyIndex.communityInfo') }}</span>
    </div>
    <div class="flex justify-between border-bottom index-1-left-1-content">
      <div v-for="(item, index) in stats" :key="index" class="flex justify-center" >
        <div class="index-1-left-img">
          <i :class="item.icon"></i>
        </div>
        <div>
          <div class="index-number">{{ indexCommunityViewInfo[item.key] }}</div>
          <div class="index-number-describe">{{ $t(`propertyIndex.${item.label}`) }}</div>
        </div>
      </div>
    </div>
    <div class="flex justify-between index-1-left-1-bottom">
      <div class="flex justify-between index-1-left-1-bottom-item">
        <div class="text-center">
          <div class="index-name">{{ $t('propertyIndex.residentReg') }}</div>
          <div class="index-name-ico">
            <i class="el-icon-user"></i>
          </div>
        </div>
        <div class="text-center">
          <div class="index-number">{{ indexCommunityViewInfo.ownerCount }}
            <span class="index-number-unit">{{ $t('propertyIndex.unit') }}</span>
          </div>
          <div class="index-number-describe margin-top-sm" @click="_toOwner">
            {{ $t('propertyIndex.registerResident') }} >
          </div>
        </div>
      </div>
      <div class="flex justify-between index-1-left-1-bottom-item">
        <div class="text-center">
          <div class="index-name">{{ $t('propertyIndex.vehicle') }}</div>
          <div class="index-name-ico">
            <i class="el-icon-truck"></i>
          </div>
        </div>
        <div class="text-center">
          <div class="index-number">{{ indexCommunityViewInfo.carCount }}
            <span class="index-number-unit">{{ $t('propertyIndex.unit') }}</span>
          </div>
          <div class="index-number-describe margin-top-sm" @click="_toCar">
            {{ $t('propertyIndex.viewVehicle') }} >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getPropertyAssetsIndex } from '@/api/index/propertyIndexApi'

export default {
  name: 'IndexCommunity',
  data() {
    return {
      indexCommunityViewInfo: {
        floorCount: 0,
        roomCount: 0,
        shopCount: 0,
        ownerCount: 0,
        spaceCount: 0,
        carCount: 0,
      },
      stats: [
        { key: 'floorCount', label: 'building', icon: 'el-icon-office-building' },
        { key: 'roomCount', label: 'house', icon: 'el-icon-house' },
        { key: 'shopCount', label: 'shop', icon: 'el-icon-shopping-bag-2' },
        { key: 'spaceCount', label: 'parkingSpace', icon: 'el-icon-truck' }
      ]
    }
  },
  created() {
  },
  methods: {
    _loadPropertyIndexAssets() {
      const param = {
        page: 1,
        row: 10,
        communityId: this.getCommunityId()
      }

      getPropertyAssetsIndex(param)
        .then(res => {
          if (res.code === 0) {
            Object.assign(this.indexCommunityViewInfo, res.data)
          }
        })
        .catch(err => {
          console.error('请求失败处理', err)
        })
    },
    _toOwner() {
      this.$router.push('/pages/property/listOwner?tab=ownerInfo')
    },
    _toCar() {
      this.$router.push('/pages/property/listOwnerCar?tab=ownerCar')
    }
  }
}
</script>

<style lang="scss" scoped>
.index-1-left-1 {
  background: #fff;
  border-radius: 4px;
  padding: 15px;
  
  .index-title {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 15px;
    text-align: left;
    color: #333;
  }
  
  .index-1-left-1-content {
    padding: 50px 0;
    border-bottom: 1px solid #eee;
    
    > div {
      width: 25%;
      text-align: center;
    }
    
    .index-1-left-img {
      width: 40px;
      height: 40px;
      background: #f0f7ff;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 10px;
      
      i {
        font-size: 20px;
        color: #409EFF;
      }
    }
    
    .index-number {
      font-size: 24px;
      font-weight: bold;
      color: #333;
    }
    
    .index-number-describe {
      font-size: 12px;
      color: #999;
    }
  }
  
  .index-1-left-1-bottom {
    padding: 28px 40px;
    
    .index-1-left-1-bottom-item {
      width: 40%;
      border-radius: 4px;
      padding: 10px;
      padding-left:30px;
      padding-right:30px;
      
      .index-name {
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
      
      .index-name-ico {
        width: 40px;
        height: 40px;
        background: #e6f7ff;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 10px auto;
        
        i {
          font-size: 20px;
          color: #1890ff;
        }
      }
      
      .index-number {
        font-size: 24px;
        font-weight: bold;
        color: #333;
        
        .index-number-unit {
          font-size: 14px;
          color: #999;
        }
      }
      
      .index-number-describe {
        font-size: 12px;
        color: #1890ff;
        cursor: pointer;
        
        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
  
  .margin-top-sm {
    margin-top: 5px;
  }
}
</style>