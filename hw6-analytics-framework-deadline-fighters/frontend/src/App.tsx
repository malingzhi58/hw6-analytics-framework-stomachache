/* eslint-disable no-param-reassign */
import {
  Box,
  Button,
  Container,
  ToggleButton,
  ToggleButtonGroup,
  ToggleButtonGroupProps,
  Typography,
} from '@mui/material';
import axios, { AxiosError } from 'axios';
import React, { useCallback, useEffect, useState } from 'react';
import './App.css';
import DataPluginForm from './components/DataPluginForm';
import VisualizationPluginDisplay from './components/VisualizationPluginDisplay';
import { Plugin } from './interfaces/plugin';
import { DataPluginInput } from './interfaces/data-plugin-input';
import { ImportDataResult } from './interfaces/import-data-result';
import { VisualizationPluginSupport } from './interfaces/visualization-plugin-support';

const App: React.FC = () => {
  const [dataPlugins, setDataPlugins] = useState<Plugin[]>([]);
  useEffect(() => {
    axios.get<Plugin[]>('/api/plugins/data').then(({ data }) => setDataPlugins(data)).catch(console.error);
  }, []);

  const [dataPluginInputs, setDataPluginInputs] = useState<DataPluginInput[]>([{ id: 0, key: '', name: '', arg: '', symbols: '' }]);
  const [importDataResult, setImportDataResult] = useState<ImportDataResult>();

  const [submitError, setSubmitError] = useState<string>();
  useEffect(() => setSubmitError(undefined), [dataPluginInputs]);

  const handleDataPluginInputChange = useCallback((index: number, input: DataPluginInput) => {
    setDataPluginInputs(([...inputs]) => {
      inputs.splice(index, 1, input);
      return inputs;
    });
  }, [setDataPluginInputs]);

  const handleDataPluginInputDelete = useCallback((index: number) => {
    setDataPluginInputs(([...inputs]) => {
      inputs.splice(index, 1);
      return inputs;
    });
  }, [setDataPluginInputs]);

  const handleDataPluginAdd = useCallback(() => {
    setDataPluginInputs((inputs) => [
      ...inputs,
      { id: inputs.length ? inputs[inputs.length - 1].id + 1 : 0, key: '', name: '', arg: '', symbols: '' },
    ]);
  }, [setDataPluginInputs]);

  const handleSubmit = useCallback(() => {
    setImportDataResult(undefined);
    if (dataPluginInputs.length === 0) {
      setSubmitError('No data plugin defined');
    } else if (dataPluginInputs.some((i) => !i.name)) {
      setSubmitError('Data plugin(s) is not set');
    } else if (dataPluginInputs.some((i) => !i.symbols)) {
      setSubmitError('Symbols are not set');
    } else {
      setSubmitError(undefined);
      axios.post<ImportDataResult>('/api/plugins/data/import', dataPluginInputs)
        .then(({ data }) => setImportDataResult(data))
        .catch((e: AxiosError) => {
          setSubmitError(e.response?.data as string);
        });
    }
  }, [dataPluginInputs]);

  const [supportedVisualizationPlugins, setSupportedVisualizationPlugins] = useState<Plugin[]>();
  const [unsupportedVisualizationPlugins, setUnsupportedVisualizationPlugins] = useState<Plugin[]>();
  const [selectedVisualizationPlugin, setSelectedVisualizationPlugin] = useState<Plugin>();
  const [visualizationHtml, setVisualizationHtml] = useState<string>();
  const [visualizationError, setVisualizationError] = useState<string>();
  useEffect(() => {
    setVisualizationHtml(undefined);
    setVisualizationError(undefined);
    if (selectedVisualizationPlugin) {
      axios.post<string>('/api/plugins/visualization/render', selectedVisualizationPlugin)
        .then(({ data }) => setVisualizationHtml(data))
        .catch((e: AxiosError) => {
          setVisualizationError(e.response?.data as string);
        });
    }
  }, [selectedVisualizationPlugin]);
  const handleSelectedVisualizationPluginChange: ToggleButtonGroupProps['onChange'] = useCallback((event, value) => {
    setSelectedVisualizationPlugin(supportedVisualizationPlugins?.find((p) => p.key === value));
  }, [supportedVisualizationPlugins, setSelectedVisualizationPlugin]);

  useEffect(() => {
    setSupportedVisualizationPlugins(undefined);
    setUnsupportedVisualizationPlugins(undefined);
    setSelectedVisualizationPlugin(undefined);
    if (importDataResult) {
      axios.get<VisualizationPluginSupport>('/api/plugins/visualization').then(({ data }) => {
        const { supported, unsupported } = data;
        setSupportedVisualizationPlugins(supported);
        setUnsupportedVisualizationPlugins(unsupported);
      }).catch(console.error);
    }
  }, [importDataResult]);

  return (
    <Container>
      <Box paddingTop="20px">Data plugins</Box>
      {dataPluginInputs.map((value, index) => (
        <DataPluginForm
          key={value.id}
          index={index}
          input={value}
          onChange={handleDataPluginInputChange}
          onDelete={handleDataPluginInputDelete}
          plugins={dataPlugins}
          stockQuotesResult={importDataResult?.stockQuotesResults[index]}
        />
      ))}
      <Box>
        <Button onClick={handleDataPluginAdd}>Add data plugin</Button>
      </Box>
      <Box display="flex" alignItems="center">
        <Button onClick={handleSubmit} variant="contained">Submit</Button>
        <Box paddingLeft="20px" color="red">{submitError}</Box>
      </Box>
      {
        supportedVisualizationPlugins && unsupportedVisualizationPlugins && (
          <Box sx={{ paddingTop: '20px' }}>
            <Typography>Visualization:</Typography>
            {supportedVisualizationPlugins.length && (
              <ToggleButtonGroup
                color="primary"
                value={selectedVisualizationPlugin?.key || null}
                exclusive
                onChange={handleSelectedVisualizationPluginChange}
              >
                {supportedVisualizationPlugins.map((p) => (
                  <ToggleButton key={p.key} value={p.key}>{p.name}</ToggleButton>
                ))}
                {unsupportedVisualizationPlugins.map((p) => (
                  <ToggleButton key={p.key} value={p.key} disabled>{`${p.name} (unsupported)`}</ToggleButton>
                ))}
              </ToggleButtonGroup>
            )}
            {supportedVisualizationPlugins.length === 0 && unsupportedVisualizationPlugins.length === 0 && (
              <Typography>No visualization available</Typography>
            )}
            {visualizationHtml && <VisualizationPluginDisplay html={visualizationHtml} />}
            {visualizationError && <Typography sx={{ color: 'red' }}>{visualizationError}</Typography>}
          </Box>
        )
      }

    </Container>
  );
};

export default App;
