import { Plugin } from './plugin';

export interface VisualizationPluginSupport {
  supported: Plugin[];
  unsupported: Plugin[];
}
